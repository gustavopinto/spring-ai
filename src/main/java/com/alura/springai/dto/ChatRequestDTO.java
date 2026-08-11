package com.alura.springai.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(

        @NotBlank
        String mensagem,

        @NotBlank
        String emailCliente
) {
}
