package com.alura.springai.controller;

import com.alura.springai.agent.AgenteDeAtendimento;
import com.alura.springai.dto.ChatRequestDTO;
import com.alura.springai.dto.ChatResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AgenteDeAtendimento agente;

    @PostMapping
    public ResponseEntity<ChatResponseDTO> conversar(@RequestBody @Valid ChatRequestDTO request) {
        // TODO (Aula 1): chamar agente.atender(...) e devolver um ChatResponseDTO.
        throw new UnsupportedOperationException("TODO (Aula 1)");
    }
}
