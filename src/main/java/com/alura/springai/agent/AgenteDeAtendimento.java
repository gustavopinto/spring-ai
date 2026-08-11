package com.alura.springai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AgenteDeAtendimento {

    private static final String PROMPT_DE_SISTEMA = """
            TODO (Aula 1): escreva o prompt de sistema.

            Um roteiro que funciona bem, um parágrafo para cada item:

              1. PAPEL   — "Você é o agente de atendimento da loja X, responsável
                            por dúvidas sobre pedidos, entregas e reembolsos."

              2. TOM     — objetivo e cordial, sem prometer o que não pode cumprir.

              3. LIMITES — nunca invente código de pedido, status ou prazo de
                           entrega. Se não souber, diga que não tem a informação.

              4. ESCOPO  — só fala sobre pedidos desta loja; recusa educadamente
                           qualquer outro assunto.
            """;

    private final ChatClient chatClient;

    /**
     * O ChatClient.Builder é registrado pelo starter do OpenAI a partir do
     * application.yml. O que vale para toda conversa entra aqui, com os
     * métodos {@code default*}.
     */
    public AgenteDeAtendimento(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem(PROMPT_DE_SISTEMA)
                .build();
    }

    /**
     * @param emailCliente vem da sessão autenticada, nunca do texto da mensagem
     */
    public String atender(String mensagem, String emailCliente) {
        log.info("Mensagem recebida de {}", emailCliente);

        // TODO (Aula 1): chamar o modelo e devolver o texto da resposta.
        //
        //   chatClient.prompt().user(...).call().content()
        //
        // Experimente passar só a mensagem, e depois passar também quem é o
        // cliente. A resposta muda de qualidade.
        throw new UnsupportedOperationException("TODO (Aula 1)");
    }
}
