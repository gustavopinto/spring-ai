package com.alura.springai.agent;

import com.alura.springai.agent.DecisaoDoAgente.TipoDeAcao;
import com.alura.springai.domain.Pedido;
import com.alura.springai.domain.StatusPedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * As regras de negócio, em Java puro. O modelo sugere; esta classe decide.
 *
 * <p>Entra um Pedido, sai uma decisão — testável sem chamar a API do modelo.
 */
@Component
public class PoliticaDeAtendimento {

    public static final int PRAZO_REEMBOLSO_EM_DIAS = 30;

    public static final int DIAS_DE_ATRASO_PARA_ABRIR_TICKET = 3;

    /** Acima deste valor, quem decide é gente. */
    public static final BigDecimal TETO_REEMBOLSO_AUTOMATICO = new BigDecimal("500.00");

    public DecisaoDoAgente podeReembolsar(Pedido pedido) {
        String codigo = pedido.getCodigo();

        if (pedido.isReembolsado()) {
            return DecisaoDoAgente.negar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo,
                    "Este pedido já foi reembolsado anteriormente.");
        }

        if (pedido.getStatus() == StatusPedido.AGUARDANDO_PAGAMENTO
                || pedido.getStatus() == StatusPedido.CANCELADO) {
            return DecisaoDoAgente.negar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo,
                    "Não houve cobrança neste pedido, portanto não há valor a devolver.");
        }

        // Extravio é falha da loja: devolve o dinheiro independente de prazo.
        if (pedido.getStatus() == StatusPedido.EXTRAVIADO) {
            return DecisaoDoAgente.aprovar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo,
                    "Pedido extraviado no transporte — reembolso permitido independente do prazo.");
        }

        if (pedido.getStatus() != StatusPedido.ENTREGUE) {
            return DecisaoDoAgente.negar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo,
                    "O pedido ainda não foi entregue. O caminho aqui é acompanhar a entrega.");
        }

        long dias = pedido.diasDesdeACompra();
        if (dias > PRAZO_REEMBOLSO_EM_DIAS) {
            return DecisaoDoAgente.negar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo,
                    "A compra foi há %d dias e o prazo de reembolso é de %d dias."
                            .formatted(dias, PRAZO_REEMBOLSO_EM_DIAS));
        }

        if (pedido.getValorTotal().compareTo(TETO_REEMBOLSO_AUTOMATICO) > 0) {
            return DecisaoDoAgente.negar(TipoDeAcao.ESCALAR_PARA_HUMANO, codigo,
                    "Valor de R$ %s acima do teto automático de R$ %s — precisa de análise humana."
                            .formatted(pedido.getValorTotal(), TETO_REEMBOLSO_AUTOMATICO));
        }

        return DecisaoDoAgente.aprovar(TipoDeAcao.SOLICITAR_REEMBOLSO, codigo,
                "Pedido entregue há %d dias, dentro do prazo de %d dias."
                        .formatted(dias, PRAZO_REEMBOLSO_EM_DIAS));
    }

    /** Só quando existe problema concreto — senão o agente abre chamado para tudo. */
    public DecisaoDoAgente podeAbrirTicket(Pedido pedido) {
        String codigo = pedido.getCodigo();

        if (pedido.getStatus() == StatusPedido.EXTRAVIADO) {
            return DecisaoDoAgente.aprovar(TipoDeAcao.ABRIR_TICKET, codigo,
                    "Pedido extraviado — caso precisa de acompanhamento do suporte.");
        }

        var entrega = pedido.getEntrega();
        if (entrega != null && entrega.estaAtrasada()
                && entrega.diasDeAtraso() >= DIAS_DE_ATRASO_PARA_ABRIR_TICKET) {
            return DecisaoDoAgente.aprovar(TipoDeAcao.ABRIR_TICKET, codigo,
                    "Entrega atrasada em %d dias, acima do limite de %d."
                            .formatted(entrega.diasDeAtraso(), DIAS_DE_ATRASO_PARA_ABRIR_TICKET));
        }

        return DecisaoDoAgente.negar(TipoDeAcao.ABRIR_TICKET, codigo,
                "Não há problema registrado neste pedido. Informe a situação ao cliente.");
    }

    /** O texto que a tool `consultarOpcoesDisponiveis` devolve ao modelo. */
    public String orientacaoParaOModelo(Pedido pedido) {
        DecisaoDoAgente reembolso = podeReembolsar(pedido);
        DecisaoDoAgente ticket = podeAbrirTicket(pedido);

        return """
                Pedido %s · %s
                Reembolso: %s (%s)
                Abrir chamado de suporte: %s (%s)
                """.formatted(
                pedido.getCodigo(),
                pedido.getStatus().getDescricao(),
                reembolso.aprovada() ? "PERMITIDO" : "NÃO PERMITIDO",
                reembolso.justificativa(),
                ticket.aprovada() ? "PERMITIDO" : "NÃO PERMITIDO",
                ticket.justificativa());
    }
}
