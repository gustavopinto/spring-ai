package com.alura.springai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código que o cliente vê, ex.: "PED-1001". */
    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private String emailCliente;

    @Column(nullable = false)
    private String produto;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(nullable = false)
    private LocalDateTime dataPedido;

    @Embedded
    private Entrega entrega;

    /** Barra a ação repetida na Aula 4. */
    @Builder.Default
    private boolean reembolsado = false;

    public long diasDesdeACompra() {
        return ChronoUnit.DAYS.between(dataPedido.toLocalDate(), LocalDate.now());
    }
}
