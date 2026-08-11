package com.alura.springai.agent;

import com.alura.springai.dto.PedidoDTO;
import com.alura.springai.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Tools de consulta: nada aqui altera estado. As ações ficam na Aula 4, em
 * classe separada.
 *
 * <p>O texto de {@code @Tool(description)} é o que o modelo lê para decidir
 * quando chamar cada método — trate como documentação de API pública.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoConsultaTools {

    private final PedidoService pedidoService;

    @Tool(description = """
            Busca um pedido pelo código, no formato PED-0000, e retorna todos os
            seus dados: cliente, produto, valor, status atual e informações de
            entrega. Use sempre que o cliente mencionar um código de pedido.
            """)
    public Object buscarPedido(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] buscarPedido({})", codigo);

        // TODO (Aula 2): delegar para pedidoService.buscarPorCodigo(codigo).
        //
        // A decisão interessante é o que fazer quando o pedido não existe. Não
        // lance exceção crua e não devolva null em silêncio: o retorno da tool
        // vira texto que o modelo precisa conseguir explicar ao cliente.
        throw new UnsupportedOperationException("TODO (Aula 2)");
    }

    @Tool(description = """
            Retorna o status atual de um pedido e o que esse status significa.
            Use quando o cliente perguntar onde está o pedido ou qual o andamento.
            """)
    public String consultarStatus(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] consultarStatus({})", codigo);

        // TODO (Aula 2): uma linha legível, por exemplo
        //   "PED-1003 · ENVIADO — Enviado, a caminho do endereço de entrega"
        throw new UnsupportedOperationException("TODO (Aula 2)");
    }

    @Tool(description = """
            Retorna a previsão de entrega de um pedido, se ele já foi despachado,
            e informa se a entrega está atrasada e há quantos dias.
            Use quando o cliente perguntar sobre prazo, atraso ou rastreio.
            """)
    public String consultarPrazoDeEntrega(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] consultarPrazoDeEntrega({})", codigo);

        // TODO (Aula 2): três situações diferentes, e o texto precisa
        //                distinguir as três —
        //   a) ainda não despachado  (entrega == null)
        //   b) a caminho, no prazo
        //   c) atrasado — informar quantos dias, insumo da Aula 3
        throw new UnsupportedOperationException("TODO (Aula 2)");
    }

    @Tool(description = """
            Lista todos os pedidos de um cliente a partir do e-mail dele.
            Use quando o cliente não souber informar o código do pedido.
            """)
    public List<PedidoDTO> listarPedidosDoCliente(
            @ToolParam(description = "E-mail do cliente") String email) {

        log.info("[TOOL] listarPedidosDoCliente({})", email);

        // TODO (Aula 2): delegar para pedidoService.listarPorCliente(email).
        //
        // Guarde a inquietação para a Aula 5: este e-mail chega vindo do modelo,
        // ou seja, do que o cliente digitou. Nada impede alguém de pedir "liste
        // os pedidos de fulano@outro.com".
        throw new UnsupportedOperationException("TODO (Aula 2)");
    }
}
