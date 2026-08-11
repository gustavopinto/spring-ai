/*
 * Projeto base · Ciclo de vida de um pedido.
 *
 * Este enum vira a base das regras de negócio na Aula 3: o que pode ser feito
 * com um pedido depende quase sempre do status em que ele está.
 */
package com.alura.springai.domain;

public enum StatusPedido {

    AGUARDANDO_PAGAMENTO("Aguardando pagamento"),
    PAGO("Pagamento confirmado"),
    EM_SEPARACAO("Em separação no centro de distribuição"),
    ENVIADO("Enviado, a caminho do endereço de entrega"),
    ENTREGUE("Entregue ao destinatário"),
    CANCELADO("Cancelado"),
    EXTRAVIADO("Extraviado durante o transporte");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
