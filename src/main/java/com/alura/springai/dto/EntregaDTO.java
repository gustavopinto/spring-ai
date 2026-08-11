package com.alura.springai.dto;

import com.alura.springai.domain.Entrega;

import java.time.LocalDate;

/**
 * `atrasada` e `diasDeAtraso` vêm calculados. Não deixe o modelo comparar datas
 * para descobrir se houve atraso — é conta que o Java faz melhor.
 */
public record EntregaDTO(
        String transportadora,
        String codigoRastreio,
        LocalDate previsaoEntrega,
        LocalDate dataEntrega,
        boolean atrasada,
        long diasDeAtraso
) {

    /** Devolve null quando o pedido ainda não foi despachado. */
    public static EntregaDTO de(Entrega entrega) {
        if (entrega == null) {
            return null;
        }
        return new EntregaDTO(
                entrega.getTransportadora(),
                entrega.getCodigoRastreio(),
                entrega.getPrevisaoEntrega(),
                entrega.getDataEntrega(),
                entrega.estaAtrasada(),
                entrega.diasDeAtraso()
        );
    }
}
