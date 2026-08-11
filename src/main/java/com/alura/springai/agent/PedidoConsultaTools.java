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
    private final PoliticaDeAtendimento politica;

    @Tool(description = """
            Busca um pedido pelo código, no formato PED-0000, e retorna todos os
            seus dados: cliente, produto, valor, status atual e informações de
            entrega. Use sempre que o cliente mencionar um código de pedido.
            """)
    public Object buscarPedido(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] buscarPedido({})", codigo);

        return pedidoService.buscarPorCodigo(codigo)
                .map(pedido -> (Object) pedido)
                .orElse("Nenhum pedido encontrado com o código " + codigo + ".");
    }

    @Tool(description = """
            Retorna o status atual de um pedido e o que esse status significa.
            Use quando o cliente perguntar onde está o pedido ou qual o andamento.
            """)
    public String consultarStatus(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] consultarStatus({})", codigo);

        return pedidoService.buscarPorCodigo(codigo)
                .map(p -> "%s · %s — %s".formatted(p.codigo(), p.status(), p.descricaoDoStatus()))
                .orElse("Nenhum pedido encontrado com o código " + codigo + ".");
    }

    @Tool(description = """
            Retorna a previsão de entrega de um pedido, se ele já foi despachado,
            e informa se a entrega está atrasada e há quantos dias.
            Use quando o cliente perguntar sobre prazo, atraso ou rastreio.
            """)
    public String consultarPrazoDeEntrega(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] consultarPrazoDeEntrega({})", codigo);

        var pedido = pedidoService.buscarPorCodigo(codigo).orElse(null);
        if (pedido == null) {
            return "Nenhum pedido encontrado com o código " + codigo + ".";
        }
        var entrega = pedido.entrega();
        if (entrega == null) {
            return "%s ainda não foi despachado, portanto não há previsão de entrega."
                    .formatted(codigo);
        }
        if (entrega.dataEntrega() != null) {
            return "%s foi entregue em %s.".formatted(codigo, entrega.dataEntrega());
        }
        if (entrega.atrasada()) {
            return "%s está atrasado em %d dia(s). Previsão era %s, transportadora %s, rastreio %s."
                    .formatted(codigo, entrega.diasDeAtraso(), entrega.previsaoEntrega(),
                            entrega.transportadora(), entrega.codigoRastreio());
        }
        return "%s está a caminho, previsão de entrega em %s, transportadora %s, rastreio %s."
                .formatted(codigo, entrega.previsaoEntrega(),
                        entrega.transportadora(), entrega.codigoRastreio());
    }

    @Tool(description = """
            Lista todos os pedidos de um cliente a partir do e-mail dele.
            Use quando o cliente não souber informar o código do pedido.
            """)
    public List<PedidoDTO> listarPedidosDoCliente(
            @ToolParam(description = "E-mail do cliente") String email) {

        log.info("[TOOL] listarPedidosDoCliente({})", email);

        // Retomar na Aula 5: este e-mail chega vindo do modelo, ou seja, do que
        // o cliente digitou.
        return pedidoService.listarPorCliente(email);
    }

    @Tool(description = """
            Informa quais ações estão disponíveis para um pedido — se ele pode ser
            reembolsado, se cabe abrir um chamado de suporte — e o motivo de cada
            resposta.
            Consulte SEMPRE esta ferramenta antes de prometer qualquer providência
            ao cliente. Ela é a fonte da verdade sobre o que a loja permite.
            """)
    public String consultarOpcoesDisponiveis(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL] consultarOpcoesDisponiveis({})", codigo);

        return pedidoService.buscarEntidade(codigo)
                .map(politica::orientacaoParaOModelo)
                .orElse("Nenhum pedido encontrado com o código " + codigo + ".");
    }
}
