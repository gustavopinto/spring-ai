package com.alura.springai.agent;

/**
 * A resposta do back-end para uma ação que o modelo sugeriu.
 *
 * O modelo pode pedir o que quiser; quem preenche este record é o Java.
 */
public record Decisao(boolean aprovada, String motivo) {

    public static Decisao aprovar(String motivo) {
        return new Decisao(true, motivo);
    }

    public static Decisao negar(String motivo) {
        return new Decisao(false, motivo);
    }
}
