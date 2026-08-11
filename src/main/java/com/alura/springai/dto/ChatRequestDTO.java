package com.alura.springai.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(

        @NotBlank
        String mensagem,

        @NotBlank
        String emailCliente,

        /** Opcional na primeira mensagem — o controller gera um e devolve. */
        String conversationId
) {
}
