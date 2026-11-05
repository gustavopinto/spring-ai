package com.alura.springai.agent;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.alura.springai.domain.StatusPedido;
import com.alura.springai.dto.PedidoDTO;

/**
 * Regra de negócio em Java puro. Não conhece o modelo, não conhece o prompt.
 *
 * Se a regra estivesse só no prompt, seria uma sugestão: o modelo poderia
 * interpretar, arredondar ou ceder à insistência do cliente. Aqui ela é regra.
 */
@Component
public class RegrasDeReembolso {

    /** Direito de arrependimento em compras online (CDC, art. 49). */
    public static final int PRAZO_EM_DIAS = 7;

    public Decisao avaliar(PedidoDTO pedido) {
        StatusPedido status = StatusPedido.valueOf(pedido.status());

        return switch (status) {
            case EXTRAVIADO -> Decisao.aprovar(
                    "O pedido foi extraviado pela transportadora, então o reembolso é garantido.");

            case AGUARDANDO_PAGAMENTO, CANCELADO -> Decisao.negar(
                    "Não houve cobrança neste pedido, então não há valor a reembolsar.");

            case PAGO, EM_SEPARACAO, ENVIADO -> Decisao.negar(
                    "O pedido ainda não foi entregue. O reembolso só pode ser pedido após a entrega.");

            case ENTREGUE -> avaliarPrazo(pedido);
        };
    }

    private Decisao avaliarPrazo(PedidoDTO pedido) {
        LocalDate dataEntrega = pedido.entrega().dataEntrega();
        long diasDesdeAEntrega = ChronoUnit.DAYS.between(dataEntrega, LocalDate.now());

        if (diasDesdeAEntrega > PRAZO_EM_DIAS) {
            return Decisao.negar("O pedido foi entregue há " + diasDesdeAEntrega
                    + " dias, e o prazo para reembolso é de " + PRAZO_EM_DIAS + " dias.");
        }
        return Decisao.aprovar("O pedido foi entregue há " + diasDesdeAEntrega
                + " dias, dentro do prazo de " + PRAZO_EM_DIAS + " dias.");
    }
}
