package com.alura.springai.agent;

import com.alura.springai.domain.Pedido;
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

    /**
     * Cascata a implementar, nesta ordem:
     * <ol>
     *   <li>AGUARDANDO_PAGAMENTO ou CANCELADO → nega, não houve cobrança;</li>
     *   <li>EXTRAVIADO → aprova sempre, independente de prazo;</li>
     *   <li>ainda não entregue → nega, é caso de acompanhar a entrega;</li>
     *   <li>passou de {@link #PRAZO_REEMBOLSO_EM_DIAS} dias → nega;</li>
     *   <li>acima do {@link #TETO_REEMBOLSO_AUTOMATICO} → ESCALAR_PARA_HUMANO;</li>
     *   <li>caso contrário → aprova.</li>
     * </ol>
     * Toda saída precisa de justificativa em português: é ela que o cliente ouve.
     */
    public DecisaoDoAgente podeReembolsar(Pedido pedido) {
        // TODO (Aula 3)
        throw new UnsupportedOperationException("TODO (Aula 3)");
    }

    /**
     * Só quando existe problema concreto: entrega atrasada acima de
     * {@link #DIAS_DE_ATRASO_PARA_ABRIR_TICKET} dias, ou pedido EXTRAVIADO.
     *
     * <p>É o que evita o vício clássico do agente: abrir chamado para tudo,
     * porque abrir chamado parece prestativo.
     */
    public DecisaoDoAgente podeAbrirTicket(Pedido pedido) {
        // TODO (Aula 3)
        throw new UnsupportedOperationException("TODO (Aula 3)");
    }

    /**
     * O texto que a tool `consultarOpcoesDisponiveis` devolve ao modelo. Algo como:
     * <pre>
     *   Pedido PED-1003 · ENVIADO · atrasado há 5 dias.
     *   Abrir chamado de suporte: PERMITIDO (atraso acima de 3 dias).
     *   Reembolso: NÃO PERMITIDO (pedido ainda não foi entregue).
     * </pre>
     * Quem lê é um modelo de linguagem: frases curtas, veredito explícito,
     * motivo junto.
     */
    public String orientacaoParaOModelo(Pedido pedido) {
        // TODO (Aula 3)
        throw new UnsupportedOperationException("TODO (Aula 3)");
    }
}
