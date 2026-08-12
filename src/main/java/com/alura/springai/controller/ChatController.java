package com.alura.springai.controller;

import com.alura.springai.agent.AgenteDeAtendimento;
import com.alura.springai.agent.ConfirmacaoService;
import com.alura.springai.agent.ConfirmacaoService.AcaoPendente;
import com.alura.springai.dto.ChatRequestDTO;
import com.alura.springai.dto.ChatResponseDTO;
import com.alura.springai.service.AtendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
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
        String conversationId = request.conversationId();

        var pendente = confirmacaoService.consumirPendente(conversationId, request.emailCliente());
        if (pendente.isEmpty()) {
            return ResponseEntity.ok(new ChatResponseDTO(
                    "Não há nenhuma solicitação pendente de confirmação. Se ainda precisar de "
                            + "ajuda, me diga o que você gostaria de fazer.", conversationId));
        }

        try {
            return ResponseEntity.ok(new ChatResponseDTO(executar(pendente.get()), conversationId));

        } catch (Exception e) {
            // Nunca responder "pronto!" sem ter certeza — é a pior mentira que
            // um agente pode contar.
            log.error("Falha ao executar ação confirmada na conversa {}", conversationId, e);
            return ResponseEntity.ok(new ChatResponseDTO(
                    "Não consegui concluir a operação: " + e.getMessage()
                            + " Nada foi alterado no seu pedido.", conversationId));
        }
    }

    private String executar(AcaoPendente pendente) {
        return switch (pendente.tipo()) {
            case SOLICITAR_REEMBOLSO -> {
                Long protocolo = atendimentoService.reembolsar(pendente.codigoPedido());
                yield "Reembolso do pedido %s registrado sob o protocolo #%d. O valor volta para "
                        .formatted(pendente.codigoPedido(), protocolo)
                        + "a forma de pagamento original em até 5 dias úteis.";
            }
            case ABRIR_TICKET -> {
                Long protocolo = atendimentoService.abrirTicket(
                        pendente.codigoPedido(), pendente.justificativa());
                yield "Chamado #%d aberto para o pedido %s. Nossa equipe entra em contato."
                        .formatted(protocolo, pendente.codigoPedido());
            }
            case ESCALAR_PARA_HUMANO -> {
                Long protocolo = atendimentoService.abrirTicket(
                        pendente.codigoPedido(), "ESCALADO: " + pendente.justificativa());
                yield "Seu caso foi encaminhado a um atendente sob o chamado #%d.".formatted(protocolo);
            }
        };
    }
}
