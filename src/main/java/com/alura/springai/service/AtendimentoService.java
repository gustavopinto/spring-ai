package com.alura.springai.service;

import com.alura.springai.agent.PoliticaDeAtendimento;
import com.alura.springai.repository.ReembolsoRepository;
import com.alura.springai.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Carregar o pedido → revalidar {@code politica.podeAbrirTicket} → se já
     * existe chamado ABERTO para este pedido, devolver o existente em vez de
     * criar outro → persistir.
     *
     * @return número do chamado criado, ou do que já existia
     */
    @Transactional
    public Long abrirTicket(String codigoPedido, String motivo) {
        log.info("[AÇÃO] abrirTicket pedido={}", codigoPedido);
        // TODO (Aula 4)
        throw new UnsupportedOperationException("TODO (Aula 4)");
    }

    /**
     * Carregar o pedido → revalidar {@code politica.podeReembolsar} → checar se
     * já existe reembolso → gravar o registro → marcar o pedido como reembolsado.
     *
     * <p>Os dois últimos passos precisam cair na MESMA transação: se o segundo
     * falhar, o primeiro tem que voltar atrás, senão fica registro de reembolso
     * com o pedido não marcado e o cliente consegue pedir de novo.
     */
    @Transactional
    public Long reembolsar(String codigoPedido) {
        log.info("[AÇÃO] reembolsar pedido={}", codigoPedido);
        // TODO (Aula 4)
        throw new UnsupportedOperationException("TODO (Aula 4)");
    }
}
