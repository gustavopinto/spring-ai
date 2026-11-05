package com.alura.springai.agent;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.alura.springai.service.PedidoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReembolsoTools {

    private final PedidoService pedidoService;
    private final RegrasDeReembolso regrasDeReembolso;

    @Tool(description = """
        Solicita o reembolso de um pedido. O sistema avalia as regras da loja e
        responde se o reembolso foi aprovado ou negado, junto com o motivo.
        Use essa tool somente depois de identificar e consultar o pedido.
        """)
    public Decisao solicitarReembolso(@ToolParam(description = "Código do pedido no formato PED-0000")
                    String codigoPedido) {

        // 1. O modelo SUGERIU a ação: ele acha que cabe um reembolso.
        log.info("[MODELO SUGERIU] reembolso do pedido {}", codigoPedido);

        // 2. O back-end VALIDA: busca o pedido de verdade e aplica a regra.
        Decisao decisao = this.pedidoService
                    .buscarPorCodigo(codigoPedido)
                    .map(regrasDeReembolso::avaliar)
                    .orElse(Decisao.negar("Pedido não encontrado para o código " + codigoPedido));

        log.info("[BACK-END DECIDIU] aprovada={} motivo={}", decisao.aprovada(), decisao.motivo());
        return decisao;
    }
}
