package com.alura.springai.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    /** Já passou da previsão e ainda não foi entregue. */
    public boolean estaAtrasada() {
        return dataEntrega == null
                && previsaoEntrega != null
                && LocalDate.now().isAfter(previsaoEntrega);
    }

    public long diasDeAtraso() {
        if (!estaAtrasada()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(previsaoEntrega, LocalDate.now());
    }
}
