package com.alura.springai.service;

import com.alura.springai.agent.DecisaoDoAgente;
import com.alura.springai.agent.PoliticaDeAtendimento;
import com.alura.springai.domain.Pedido;
import com.alura.springai.domain.Reembolso;
import com.alura.springai.domain.Ticket;
import com.alura.springai.repository.ReembolsoRepository;
import com.alura.springai.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Executa as ações. É a última barreira, e revalida a política mesmo que a tool
 * já tenha validado: o mesmo método pode ser chamado amanhã por outro caminho
 * que esqueceu de validar.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AtendimentoService {

    public static final String STATUS_ABERTO = "ABERTO";

    private final TicketRepository ticketRepository;
    private final ReembolsoRepository reembolsoRepository;
    private final PedidoService pedidoService;
    private final PoliticaDeAtendimento politica;

    /** @return número do chamado criado, ou do que já existia para este pedido */
    @Transactional
    public Long abrirTicket(String codigoPedido, String motivo) {
        log.info("[AÇÃO] abrirTicket pedido={}", codigoPedido);

        Pedido pedido = carregar(codigoPedido);

        DecisaoDoAgente decisao = politica.podeAbrirTicket(pedido);
        if (!decisao.aprovada()) {
            throw new IllegalStateException(decisao.justificativa());
        }

        var existente = ticketRepository.findFirstByCodigoPedidoAndStatus(codigoPedido, STATUS_ABERTO);
        if (existente.isPresent()) {
            log.info("Já existe chamado aberto para {}: #{}", codigoPedido, existente.get().getId());
            return existente.get().getId();
        }

        Ticket ticket = ticketRepository.save(Ticket.builder()
                .codigoPedido(codigoPedido)
                .motivo(motivo)
                .status(STATUS_ABERTO)
                .abertoEm(LocalDateTime.now())
                .abertoPeloAgente(true)
                .build());

        return ticket.getId();
    }

    /**
     * O registro do reembolso e a marcação do pedido caem na mesma transação: se
     * a segunda falhar, a primeira volta atrás, senão fica reembolso registrado
     * com o pedido não marcado e o cliente consegue pedir de novo.
     */
    @Transactional
    public Long reembolsar(String codigoPedido) {
        log.info("[AÇÃO] reembolsar pedido={}", codigoPedido);

        Pedido pedido = carregar(codigoPedido);

        DecisaoDoAgente decisao = politica.podeReembolsar(pedido);
        if (!decisao.aprovada()) {
            throw new IllegalStateException(decisao.justificativa());
        }
        if (reembolsoRepository.existsByCodigoPedido(codigoPedido)) {
            throw new IllegalStateException("Já existe um reembolso registrado para " + codigoPedido);
        }

        Reembolso reembolso = reembolsoRepository.save(Reembolso.builder()
                .codigoPedido(codigoPedido)
                .valor(pedido.getValorTotal())
                .justificativaDaPolitica(decisao.justificativa())
                .solicitadoEm(LocalDateTime.now())
                .build());

        pedidoService.marcarComoReembolsado(pedido);

        return reembolso.getId();
    }

    private Pedido carregar(String codigoPedido) {
        return pedidoService.buscarEntidade(codigoPedido)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pedido não encontrado: " + codigoPedido));
    }
}
