package com.alura.springai.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/** `abertoPeloAgente` separa o que foi criado por IA do que foi criado por gente. */
@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigoPedido;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime abertoEm;

    @Builder.Default
    private boolean abertoPeloAgente = true;
}
