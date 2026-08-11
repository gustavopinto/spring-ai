/*
 * Projeto base · Dados de entrega, embutidos no Pedido.
 */
package com.alura.springai.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Entrega {

    private String transportadora;
    private String codigoRastreio;
    private LocalDate previsaoEntrega;
    private LocalDate dataEntrega;
}
