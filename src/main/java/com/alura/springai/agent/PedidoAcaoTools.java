package com.alura.springai.agent;

import com.alura.springai.agent.ConfirmacaoService.AcaoPendente;
import com.alura.springai.agent.DecisaoDoAgente.TipoDeAcao;
import com.alura.springai.domain.Pedido;
import com.alura.springai.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Tools que alteram estado — separadas das de consulta de propósito. Consulta
 * chamada duas vezes não custa nada; ação chamada duas vezes reembolsa duas vezes.
 *
 * <p>Nenhum método aqui executa. Todos validam na política, registram um
 * pendente e devolvem AGUARDANDO_CONFIRMACAO. Quem executa é /api/chat/confirmar.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoAcaoTools {

    private final PedidoService pedidoService;
    private final PoliticaDeAtendimento politica;
    private final ConfirmacaoService confirmacaoService;

    /**
     * A conversa em curso, definida pelo agente antes de chamar o modelo.
     * Fica fora da assinatura das tools de propósito: o id da conversa e a
     * identidade do cliente não podem vir do modelo.
     */
    private static final ThreadLocal<String> CONVERSA_ATUAL = new ThreadLocal<>();
    private static final ThreadLocal<String> CLIENTE_ATUAL = new ThreadLocal<>();

    public static void definirContexto(String conversationId, String emailCliente) {
        CONVERSA_ATUAL.set(conversationId);
        CLIENTE_ATUAL.set(emailCliente);
    }

    public static void limparContexto() {
        CONVERSA_ATUAL.remove();
        CLIENTE_ATUAL.remove();
    }

    @Tool(description = """
            Prepara a abertura de um chamado de suporte para um pedido com
            problema, como atraso na entrega ou extravio. NÃO abre o chamado
            imediatamente: valida se é permitido e pede a confirmação do cliente.
            Use apenas depois de consultar o pedido e as opções disponíveis.
            """)
    public ResultadoAcao abrirChamadoDeSuporte(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo,
            @ToolParam(description = "Motivo objetivo do chamado") String motivo) {

        log.info("[TOOL-AÇÃO] abrirChamadoDeSuporte({})", codigo);

        Pedido pedido = pedidoService.buscarEntidade(codigo).orElse(null);
        if (pedido == null) {
            return ResultadoAcao.erro("Nenhum pedido encontrado com o código " + codigo + ".");
        }

        DecisaoDoAgente decisao = politica.podeAbrirTicket(pedido);
        if (!decisao.aprovada()) {
            return ResultadoAcao.negada(decisao.justificativa());
        }

        registrar(TipoDeAcao.ABRIR_TICKET, codigo, motivo);

        return ResultadoAcao.aguardandoConfirmacao(
                "Posso abrir um chamado de suporte para o pedido %s (%s)? Confirme para eu prosseguir."
                        .formatted(codigo, motivo));
    }

    @Tool(description = """
            Prepara o reembolso de um pedido. NÃO efetua o reembolso: verifica se
            o pedido é elegível segundo as regras da loja e, se for, pede
            confirmação explícita ao cliente.
            Se não for elegível, explique ao cliente o motivo informado e não
            tente novamente.
            """)
    public ResultadoAcao solicitarReembolso(
            @ToolParam(description = "Código do pedido, ex.: PED-1001") String codigo) {

        log.info("[TOOL-AÇÃO] solicitarReembolso({})", codigo);

        Pedido pedido = pedidoService.buscarEntidade(codigo).orElse(null);
        if (pedido == null) {
            return ResultadoAcao.erro("Nenhum pedido encontrado com o código " + codigo + ".");
        }

        // O "não tente novamente" da descrição da tool importa tanto quanto este
        // retorno: sem ele o modelo reinterpreta a negativa como falha temporária.
        DecisaoDoAgente decisao = politica.podeReembolsar(pedido);
        if (!decisao.aprovada()) {
            return ResultadoAcao.negada(decisao.justificativa());
        }

        registrar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo, decisao.justificativa());

        return ResultadoAcao.aguardandoConfirmacao(
                "Confirma o reembolso de R$ %s do pedido %s? Nada foi processado ainda."
                        .formatted(pedido.getValorTotal(), codigo));
    }

    @Tool(description = """
            Encaminha o caso para um atendente humano. Use quando o cliente pedir
            para falar com uma pessoa, quando o caso estiver fora das suas regras,
            ou quando você já tentou resolver e não conseguiu.
            """)
    public ResultadoAcao escalarParaHumano(
            @ToolParam(description = "Código do pedido, se houver") String codigo,
            @ToolParam(description = "Resumo do caso para o atendente") String resumo) {

        log.info("[TOOL-AÇÃO] escalarParaHumano({})", codigo);

        // A válvula de escape do agente. Sem ela, um agente encurralado inventa
        // uma solução.
        registrar(TipoDeAcao.ESCALAR_PARA_HUMANO, codigo, resumo);

        return ResultadoAcao.aguardandoConfirmacao(
                "Posso encaminhar seu caso para um atendente humano? Confirme para eu prosseguir.");
    }

    private void registrar(TipoDeAcao tipo, String codigo, String justificativa) {
        confirmacaoService.registrarPendente(CONVERSA_ATUAL.get(), new AcaoPendente(
                tipo, codigo, CLIENTE_ATUAL.get(), justificativa, Instant.now()));
    }
}
