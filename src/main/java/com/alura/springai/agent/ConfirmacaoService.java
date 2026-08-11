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

    /**
     * @param conversationId chave do pendente — uma conversa, uma ação pendente
     */
    public void registrarPendente(String conversationId, AcaoPendente acao) {
        // TODO (Aula 4): guardar no mapa.
        //
        // Substituir o pendente anterior é o comportamento correto: se o cliente
        // pediu reembolso e depois mudou de ideia e pediu um chamado, o "sim"
        // precisa valer para o pedido mais recente.
        throw new UnsupportedOperationException("TODO (Aula 4)");
    }

    /**
     * Precisa REMOVER o pendente ao devolver — uma confirmação vale uma
     * execução, senão um "sim" repetido reembolsa duas vezes. E precisa conferir
     * o e-mail, senão a confirmação de um cliente executa a ação de outro.
     */
    public Optional<AcaoPendente> consumirPendente(String conversationId, String emailCliente) {
        // TODO (Aula 4): buscar, checar expiração, conferir o e-mail, remover.
        throw new UnsupportedOperationException("TODO (Aula 4)");
    }
}
