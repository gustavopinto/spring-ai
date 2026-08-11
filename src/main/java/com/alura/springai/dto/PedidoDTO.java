package com.alura.springai.dto;

import com.alura.springai.domain.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A partir da Aula 2 este DTO também é o retorno das tools — o JSON que o
 * modelo lê.
 */
public record PedidoDTO(
        String codigo,
        String cliente,
        String produto,
        BigDecimal valorTotal,
        String status,
        String descricaoDoStatus,
        LocalDateTime dataPedido,
        EntregaDTO entrega
) {

    public static PedidoDTO de(Pedido pedido) {
        return new PedidoDTO(
                pedido.getCodigo(),
                pedido.getCliente(),
                pedido.getProduto(),
                pedido.getValorTotal(),
                pedido.getStatus().name(),
                pedido.getStatus().getDescricao(),
                pedido.getDataPedido(),
                EntregaDTO.de(pedido.getEntrega())
        );
    }
}
