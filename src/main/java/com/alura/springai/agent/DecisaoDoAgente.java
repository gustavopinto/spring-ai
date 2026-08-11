package com.alura.springai.agent;

/**
 * Separa o que se pretende fazer ({@code acao}) do que o back-end autorizou
 * ({@code aprovada}). Duas coisas diferentes, dois campos diferentes.
 */
public record DecisaoDoAgente(

        TipoDeAcao acao,

        String codigoPedido,

        /** Quem responde é a PoliticaDeAtendimento, nunca o modelo. */
        boolean aprovada,

        /** Motivo em português: volta para o modelo e vira a explicação ao cliente. */
        String justificativa
) {

    public enum TipoDeAcao {
        ABRIR_TICKET,
        SOLICITAR_REEMBOLSO,
        ESCALAR_PARA_HUMANO
    }

    public static DecisaoDoAgente aprovar(TipoDeAcao acao, String codigo, String motivo) {
        return new DecisaoDoAgente(acao, codigo, true, motivo);
    }

    public static DecisaoDoAgente negar(TipoDeAcao acao, String codigo, String motivo) {
        return new DecisaoDoAgente(acao, codigo, false, motivo);
    }
}
