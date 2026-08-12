package com.alura.springai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Guarda as ações que esperam o "sim" do cliente.
 *
 * <p>O pendente fica em Java, com identificador, fora do alcance do modelo — um
 * "sim" solto no meio da conversa pode estar respondendo a outra pergunta, e o
 * modelo pode alucinar que houve confirmação.
 *
 * <p>Mapa em memória basta em aula. Em produção isto iria para Redis ou banco.
 */
@Service
@Slf4j
public class ConfirmacaoService {

    public static final Duration VALIDADE = Duration.ofMinutes(10);

    private final ConcurrentHashMap<String, AcaoPendente> pendentes = new ConcurrentHashMap<>();

    public record AcaoPendente(
            DecisaoDoAgente.TipoDeAcao tipo,
            String codigoPedido,
            String emailCliente,
            String justificativa,
            Instant criadaEm
    ) {
        public boolean expirou() {
            return Instant.now().isAfter(criadaEm.plus(VALIDADE));
        }
    }

    /** Substituir o pendente anterior é o correto: o "sim" vale para o pedido mais recente. */
    public void registrarPendente(String conversationId, AcaoPendente acao) {
        log.info("Ação pendente registrada: {} para {} (conversa {})",
                acao.tipo(), acao.codigoPedido(), conversationId);
        pendentes.put(conversationId, acao);
    }

    /** Remove ao devolver: uma confirmação vale uma execução. */
    public Optional<AcaoPendente> consumirPendente(String conversationId, String emailCliente) {
        AcaoPendente pendente = pendentes.get(conversationId);

        if (pendente == null) {
            return Optional.empty();
        }
        if (pendente.expirou()) {
            pendentes.remove(conversationId);
            return Optional.empty();
        }
        // Sem esta checagem, a confirmação de um cliente executa a ação de outro.
        if (!pendente.emailCliente().equals(emailCliente)) {
            log.warn("Confirmação recusada: {} tentou confirmar ação de {}",
                    emailCliente, pendente.emailCliente());
            return Optional.empty();
        }

        pendentes.remove(conversationId);
        return Optional.of(pendente);
    }
}
