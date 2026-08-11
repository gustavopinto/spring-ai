package com.alura.springai.controller;

import com.alura.springai.agent.AgenteDeAtendimento;
import com.alura.springai.agent.ConfirmacaoService;
import com.alura.springai.dto.ChatRequestDTO;
import com.alura.springai.dto.ChatResponseDTO;
import com.alura.springai.service.AtendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AgenteDeAtendimento agente;
    private final ConfirmacaoService confirmacaoService;
    private final AtendimentoService atendimentoService;

    @PostMapping
    public ResponseEntity<ChatResponseDTO> conversar(@RequestBody @Valid ChatRequestDTO request) {
        String conversationId = request.conversationId() != null && !request.conversationId().isBlank()
                ? request.conversationId()
                : UUID.randomUUID().toString();

        String resposta = agente.atender(request.mensagem(), request.emailCliente(), conversationId);
        return ResponseEntity.ok(new ChatResponseDTO(resposta, conversationId));
    }

    /**
     * Endpoint separado de propósito: confirmar é um ato explícito do usuário,
     * não algo que o modelo possa inferir de um "sim" solto numa frase.
     */
    @PostMapping("/confirmar")
    public ResponseEntity<ChatResponseDTO> confirmar(@RequestBody @Valid ChatRequestDTO request) {
        // TODO (Aula 4):
        //   1. confirmacaoService.consumirPendente(conversationId, emailCliente)
        //   2. vazio → responder que não há nada pendente, sem executar nada
        //   3. conforme o tipo, chamar atendimentoService.reembolsar ou .abrirTicket
        //   4. devolver o número do protocolo
        //   5. try/catch: se a execução falhar, diga que NÃO foi concluída.
        //      Nunca responda "pronto!" sem ter certeza.
        throw new UnsupportedOperationException("TODO (Aula 4)");
    }
}
