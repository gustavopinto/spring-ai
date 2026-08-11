package com.alura.springai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AgenteDeAtendimento {

    private static final String PROMPT_DE_SISTEMA = """
            Você é o agente de atendimento de uma loja online, responsável por
            dúvidas sobre pedidos, entregas e reembolsos.

            Seja objetivo e cordial. Responda em português do Brasil, em poucas
            frases, sem prometer o que não pode cumprir.

            Nunca invente código de pedido, status, prazo ou valor. Se você não
            tem a informação, diga com clareza e explique o que o cliente pode
            fazer.

            Você só fala sobre pedidos desta loja. Para qualquer outro assunto,
            recuse educadamente e ofereça ajuda com pedidos.
            """;

    private final ChatClient chatClient;

    public AgenteDeAtendimento(ChatClient.Builder builder,
                               PedidoConsultaTools pedidoConsultaTools) {
        this.chatClient = builder
                .defaultSystem(PROMPT_DE_SISTEMA)
                // TODO (Aula 2): registrar as ferramentas com
                //
                //     .defaultTools(pedidoConsultaTools)
                //
                // É esta linha que transforma o chatbot da Aula 1 em agente: a
                // partir dela o modelo recebe, junto do prompt, a lista de
                // métodos Java que pode pedir para executar.
                .build();
    }

    /**
     * @param emailCliente vem da sessão autenticada, nunca do texto da mensagem
     */
    public String atender(String mensagem, String emailCliente) {
        log.info("Mensagem recebida de {}", emailCliente);

        String entrada = """
                %s

                (mensagem enviada pelo cliente autenticado %s)
                """.formatted(mensagem, emailCliente);

        return chatClient.prompt()
                .user(entrada)
                .call()
                .content();
    }
}
