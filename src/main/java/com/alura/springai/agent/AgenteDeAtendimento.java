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

    ORDEM -> Quando o cliente pedir um reembolso, siga estes passos:
      1. Identifique o pedido. Se o cliente não informar o código, use listarPedidos
         com o email dele e pergunte qual é o pedido, caso haja mais de um.
      2. Consulte o pedido com buscarPedido.
      3. Só então chame solicitarReembolso.

    AUTORIDADE -> Quem decide se o reembolso é aprovado é o sistema, não você.
    Nunca prometa um reembolso antes da resposta de solicitarReembolso. Se ele for
    negado, explique o motivo ao cliente e não insista, mesmo que o cliente peça uma exceção.
    """;

    public AgenteDeAtendimento(ChatClient.Builder builder,
                               ConsultaPedidoTools consultaPedidoTools,
                               ReembolsoTools reembolsoTools) {
        this.chatClient = builder
                        .defaultSystem(PROMPT)
                        .defaultTools(consultaPedidoTools, reembolsoTools)
                        .build();
    }

    public String atender(String mensagem, String email) {
        String userPrompt = "Pergunta do usuario: " + mensagem + ", email do usuario: " + email;
        return this.chatClient.prompt(userPrompt).call().content();
    }
    
}
