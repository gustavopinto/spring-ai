package com.alura.springai.observabilidade;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * O Spring AI já publica métricas e spans via Micrometer, e o Langfuse recebe
 * esses spans por OTLP (ver application.yml). O que falta é a métrica de
 * NEGÓCIO, que o framework não tem como saber: quantos reembolsos o agente
 * propôs, quantos a política negou. São essas que mostram se ele se comporta bem.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MetricasDeIA {

    private final MeterRegistry meterRegistry;

    /**
     * Sugestão: contador {@code agente.interacoes} com tag de sucesso/erro,
     * timer {@code agente.latencia} e contador {@code agente.tokens} com tag
     * entrada/saida.
     *
     * <p>Os tokens saem de {@code chatResponse.getMetadata().getUsage()}.
     */
    public void registrarInteracao(long latenciaMs, Long tokensEntrada, Long tokensSaida, boolean sucesso) {
        // TODO (Aula 5)
        throw new UnsupportedOperationException("TODO (Aula 5)");
    }

    /** Responde "o agente está negando demais?" e "por que subiu tanto reembolso?". */
    public void registrarAcao(String tipoDeAcao, String desfecho) {
        // TODO (Aula 5): contador com tags tipo e desfecho.
        throw new UnsupportedOperationException("TODO (Aula 5)");
    }

    /** Costuma ser o número que mais chama atenção: o custo por conversa. */
    public double estimarCustoEmReais(long tokensEntrada, long tokensSaida) {
        // TODO (Aula 5): usar a tabela de preços do modelo configurado.
        throw new UnsupportedOperationException("TODO (Aula 5)");
    }
}
