package com.alura.springai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AgenteDeAtendimento {

    /** Um agente que devolve stack trace é pior que um que assume a falha. */
    private static final String FALLBACK = """
            Não consegui completar seu atendimento agora por um problema técnico. \
            Nada foi alterado no seu pedido. Tente novamente em alguns instantes \
            ou fale com nosso suporte.""";

    private static final String PROMPT_DE_SISTEMA = """
            Você é o agente de atendimento de uma loja online, responsável por
            dúvidas sobre pedidos, entregas e reembolsos.

            Seja objetivo e cordial. Responda em português do Brasil, em poucas
            frases, sem prometer o que não pode cumprir.

            Nunca invente código de pedido, status, prazo ou valor. Use sempre as
            ferramentas disponíveis para obter os dados. Se não encontrar a
            informação, diga com clareza e explique o que o cliente pode fazer.

            Você só fala sobre pedidos desta loja. Para qualquer outro assunto,
            recuse educadamente e ofereça ajuda com pedidos.

            Siga sempre esta ordem diante de um problema: primeiro identifique o
            pedido, depois verifique a situação da entrega, e só então fale em
            providências. Nunca proponha uma ação antes de ter os dados.

            Antes de prometer qualquer providência ao cliente, consulte a
            ferramenta de opções disponíveis. O que ela responde vale mais do que
            a sua avaliação do caso: se ela disser que não é permitido, explique
            o motivo ao cliente e não insista.

            TODO (Aula 4): acrescente as instruções sobre ações.

              1. Reembolso e abertura de chamado NUNCA acontecem de imediato. As
                 ferramentas de ação apenas preparam a operação e devolvem um
                 pedido de confirmação — repasse a pergunta ao cliente com o
                 valor e o código do pedido, e diga que nada foi feito ainda.

              2. Quando uma ação for recusada, explique o motivo e ofereça a
                 alternativa possível. Não tente a mesma ação novamente.
            """;

    private final ChatClient chatClient;

    public AgenteDeAtendimento(ChatClient.Builder builder,
                               PedidoConsultaTools pedidoConsultaTools,
                               PedidoAcaoTools pedidoAcaoTools) {
        this.chatClient = builder
                .defaultSystem(PROMPT_DE_SISTEMA)
                .defaultTools(pedidoConsultaTools, pedidoAcaoTools)
                .build();
    }

    /**
     * @param emailCliente   vem da sessão autenticada, nunca do texto da mensagem
     * @param conversationId identifica a conversa; as tools de ação precisam
     *                       dele para guardar o pendente
     */
    public String atender(String mensagem, String emailCliente, String conversationId) {
        log.info("Mensagem recebida de {} (conversa {})", emailCliente, conversationId);

        PedidoAcaoTools.definirContexto(conversationId, emailCliente);
        try {
            String entrada = """
                    %s

                    (mensagem enviada pelo cliente autenticado %s)
                    """.formatted(mensagem, emailCliente);

            return chatClient.prompt()
                    .user(entrada)
                    .call()
                    .content();

        } catch (Exception e) {
            log.error("Falha ao atender a conversa {}", conversationId, e);
            return FALLBACK;

        } finally {
            PedidoAcaoTools.limparContexto();
        }
    }
}
