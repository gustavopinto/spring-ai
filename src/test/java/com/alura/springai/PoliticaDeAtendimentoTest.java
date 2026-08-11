package com.alura.springai;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Rápido, gratuito e determinístico — só é possível porque a regra está em
 * Java, e não no prompt. Monte os pedidos com Pedido.builder(), sem subir o
 * contexto do Spring.
 */
class PoliticaDeAtendimentoTest {

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("aprova reembolso de pedido entregue dentro do prazo de 30 dias")
    void reembolsoDentroDoPrazo() {
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("nega reembolso de pedido comprado há mais de 30 dias")
    void reembolsoForaDoPrazo() {
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("nega segundo reembolso do mesmo pedido")
    void reembolsoDuplicado() {
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("aprova reembolso de pedido extraviado mesmo fora do prazo")
    void extraviadoIgnoraPrazo() {
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("escala para humano quando o valor passa do teto automático")
    void acimaDoTetoEscala() {
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("não abre chamado para pedido dentro do prazo de entrega")
    void naoAbreChamadoSemProblema() {
    }
}
