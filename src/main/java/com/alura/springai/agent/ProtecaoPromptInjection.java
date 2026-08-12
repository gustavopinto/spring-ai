package com.alura.springai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Esta classe é UMA camada, não A proteção.
 *
 * <p>Filtro de texto sempre pode ser contornado: em outro idioma, com base64,
 * com sinônimos. O que realmente protege é a arquitetura das Aulas 3 e 4 —
 * mesmo que o injection funcione por completo, o modelo só consegue SUGERIR o
 * reembolso; a política continua negando e a confirmação continua sendo exigida.
 *
 * <p>Segurança de agente é desenho de permissões, não filtro de palavra.
 */
@Component
@Slf4j
public class ProtecaoPromptInjection {

    private static final List<String> PADROES_SUSPEITOS = List.of(
            "ignore as instruções",
            "ignore previous instructions",
            "esqueça as regras",
            "você agora é",
            "system prompt",
            "revele suas instruções",
            "aja como se"
    );

    /** Teto de mensagens por conversa — contém o abuso de custo. */
    public static final int MAX_MENSAGENS_POR_CONVERSA = 20;

    /**
     * O que fazer ao detectar é decisão de produto, e vale debater em aula:
     * bloquear gera falso positivo com cliente legítimo ("ignore o que eu disse
     * antes, o pedido certo é o 1002"); registrar e monitorar costuma ser mais
     * sensato, já que a barreira real está na política.
     */
    public boolean pareceInjection(String mensagem) {
        // TODO (Aula 5): normalizar (minúsculas, sem acento) e comparar com os
        //                padrões. Logar em WARN quando casar.
        throw new UnsupportedOperationException("TODO (Aula 5)");
    }

    /**
     * Envolve o texto do cliente em delimitadores, com o system prompt dizendo
     * que o conteúdo ali dentro é DADO, nunca ordem. Ajuda o modelo a manter a
     * fronteira; não é infalível.
     */
    public String isolarEntradaDoCliente(String mensagem) {
        // TODO (Aula 5): algo como
        //   <mensagem_do_cliente>...</mensagem_do_cliente>
        throw new UnsupportedOperationException("TODO (Aula 5)");
    }

    /**
     * Contém dois abusos diferentes: o usuário que manda mensagem sem parar, e o
     * agente que entra em ciclo de tool calling — chama a tool, não gosta do
     * resultado, chama de novo.
     *
     * <p>Para o segundo caso existe também o limite de iterações do próprio
     * Spring AI. Vale mostrar os dois em aula.
     */
    public boolean excedeuLimite(String conversationId, int mensagensAteAgora) {
        // TODO (Aula 5)
        throw new UnsupportedOperationException("TODO (Aula 5)");
    }
}
