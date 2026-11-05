package com.alura.springai.agent;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.alura.springai.dto.PedidoDTO;
import com.alura.springai.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConsultaPedidoTools {

    private final PedidoService pedidoService;

    @Tool(description = """
        Essa tool busca pedido de um código, usando o formato PED-0000. Retorna
        todas as informações do pedido, incluindo, cliente, nome do pedido, valor, 
        status da entrega, transportadora, etc. Use sempre essa tool quando o cleinte
        mencionar a ajuda com um código de pedido.
        """)
    public String buscarPedido(@ToolParam(description = "Código do pedido no formato PED-0000") 
                    String codigoPedido) {
        return this.pedidoService
                    .buscarPorCodigo(codigoPedido)
                    .map(Object::toString) 
                    .orElse("Pedido não encontrado para o código " + codigoPedido); // ponto de melhoria para LLM
    }

    @Tool(description = """
        Essa tool busca todos os pedidos de um determinado cliente, pegando pelo email do cliente.
        """)
    public List<PedidoDTO> listarPedidos(@ToolParam(description = "Email do cliente") 
                    String emailCliente) { 
        return this.pedidoService.listarPorCliente(emailCliente);
    }
}
