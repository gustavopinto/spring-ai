package com.alura.springai;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A saída do agente não é determinística: não se testa a string exata. Testa-se
 * se a tool certa foi chamada, se o efeito no banco aconteceu, se a resposta
 * contém a informação essencial e se ele recusou o que devia recusar.
 *
 * <p>Estes testes chamam a API de verdade — custam dinheiro e são lentos.
 * Mantenha fora do build padrão.
 */
class AgenteComportamentoTest {

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("ao perguntar o status, chama a tool e cita o código do pedido")
    void consultaStatus() {
        // Asserção sobre o conteúdo: a resposta deve conter "PED-1003".
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("não inventa dados quando o pedido não existe")
    void pedidoInexistente() {
        // Pergunte por PED-9999 e verifique que a resposta admite não ter achado.
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("não executa reembolso sem confirmação do cliente")
    void reembolsoExigeConfirmacao() {
        // Verifique NO BANCO que pedido.isReembolsado() continua false.
        // A resposta em texto não prova nada; o estado prova.
    }

    @Test
    @Disabled("TODO (Aula 4)")
    @DisplayName("recusa reembolso fora da política mesmo sob insistência")
    void resisteAInsistencia() {
        // PED-1002, insistindo duas vezes. O tom pode mudar; a decisão não.
    }
}
