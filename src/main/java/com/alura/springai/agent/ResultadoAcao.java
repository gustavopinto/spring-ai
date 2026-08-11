package com.alura.springai.agent;

/**
 * O que uma tool de ação devolve ao modelo.
 *
 * <p>Existe para o modelo distinguir "negado pela regra" de "falhou por erro".
 * Se tudo virar texto solto, ele trata negativa como falha temporária e chama a
 * ferramenta de novo, em loop.
 */
public record ResultadoAcao(

        Desfecho desfecho,

        /** Mensagem em português, pronta para o modelo repassar ao cliente. */
        String mensagem,

        /** Número do chamado ou do reembolso, quando houve execução. */
        Long protocolo
) {

    public enum Desfecho {
        EXECUTADA,
        /** Nada foi alterado ainda — espera o "sim" do cliente. */
        AGUARDANDO_CONFIRMACAO,
        /** A política negou. Não adianta tentar de novo. */
        NEGADA,
        ESCALADA,
        /** Erro técnico. Aqui sim faz sentido o fallback. */
        ERRO
    }

    public static ResultadoAcao executada(String mensagem, Long protocolo) {
        return new ResultadoAcao(Desfecho.EXECUTADA, mensagem, protocolo);
    }

    public static ResultadoAcao aguardandoConfirmacao(String mensagem) {
        return new ResultadoAcao(Desfecho.AGUARDANDO_CONFIRMACAO, mensagem, null);
    }

    public static ResultadoAcao negada(String motivo) {
        return new ResultadoAcao(Desfecho.NEGADA, motivo, null);
    }

    public static ResultadoAcao escalada(String mensagem, Long protocolo) {
        return new ResultadoAcao(Desfecho.ESCALADA, mensagem, protocolo);
    }

    public static ResultadoAcao erro(String mensagem) {
        return new ResultadoAcao(Desfecho.ERRO, mensagem, null);
    }
}
