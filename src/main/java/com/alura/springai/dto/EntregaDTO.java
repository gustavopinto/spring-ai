/*
 * Projeto base · Dados de entrega expostos pela API.
 */
package com.alura.springai.dto;

import com.alura.springai.domain.Entrega;

import java.time.LocalDate;

public record EntregaDTO(
        String transportadora,
        String codigoRastreio,
        LocalDate previsaoEntrega,
        LocalDate dataEntrega
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
                entrega.getDataEntrega()
        );
    }
}
