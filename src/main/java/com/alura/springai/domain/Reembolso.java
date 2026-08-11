package com.alura.springai.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** `justificativaDaPolitica` guarda por que o back-end autorizou a operação. */
@Entity
@Table(name = "reembolsos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reembolso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoPedido;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false, length = 1000)
    private String justificativaDaPolitica;

    @Column(nullable = false)
    private LocalDateTime solicitadoEm;
}
