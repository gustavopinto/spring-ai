package com.alura.springai.agent;

import com.alura.springai.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

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

        // TODO (Aula 4), nesta ordem:
        //   1. carregar o pedido → não existe? ResultadoAcao.erro(...)
        //   2. decisao = politica.podeAbrirTicket(pedido)
        //   3. !decisao.aprovada() → ResultadoAcao.negada(decisao.justificativa())
        //   4. confirmacaoService.registrarPendente(...)
        //   5. ResultadoAcao.aguardandoConfirmacao("...confirma?")
        throw new UnsupportedOperationException("TODO (Aula 4)");
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

        // TODO (Aula 4): mesmo roteiro, com politica.podeReembolsar.
        //
        // Repare no "não tente novamente" da descrição: sem essa frase o modelo
        // reinterpreta a negativa como falha temporária e chama a tool de novo.
        // Prevenção de loop começa aqui.
        //
        // Inclua o valor na confirmação: "confirma o reembolso de R$ 289,90 do
        // PED-1001?" é muito melhor que "confirma o reembolso?".
        throw new UnsupportedOperationException("TODO (Aula 4)");
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

        // TODO (Aula 4): registrar um pendente do tipo ESCALAR_PARA_HUMANO.
        //
        // É a válvula de escape do agente. Sem ela, um agente encurralado
        // inventa uma solução.
        throw new UnsupportedOperationException("TODO (Aula 4)");
    }
}
