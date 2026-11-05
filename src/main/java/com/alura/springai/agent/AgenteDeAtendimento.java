package com.alura.springai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AgenteDeAtendimento {

    private final ChatClient chatClient;

    private static final String PROMPT = """
    PAPEL -> Você deve atuar como um agente de atendimento de uma loja de vendas online,
    responsável por tirar dúvdias sobre pedidos, entregas e reembolsos.

    TOM -> Você deve responder em um tom amigavel e cordial, com seus clientes. 

    LIMITES -> Você deve sempre se ater ao contexto de uma loja de vendas online, nao 
    inveite codigos de pedidos nem prazos de entrega.

    Se você não tiver essa informação, não invente fatos; apenas diga que não sabe;
    """;

    public AgenteDeAtendimento(ChatClient.Builder builder, ConsultaPedidoTools consultaPedidoTools) {
        this.chatClient = builder
                        .defaultSystem(PROMPT)
                        .defaultTools(consultaPedidoTools)
                        .build();
    }

    public String atender(String mensagem, String email) {
        String userPrompt = "Pergunta do usuario: " + mensagem + ", email do usuario: " + email;
        return this.chatClient.prompt(userPrompt).call().content();
    }
    
}
