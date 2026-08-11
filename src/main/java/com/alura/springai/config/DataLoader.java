package com.alura.springai.config;

import com.alura.springai.domain.Entrega;
import com.alura.springai.domain.Pedido;
import com.alura.springai.domain.StatusPedido;
import com.alura.springai.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Cada pedido cobre um caminho que as Aulas 3 e 4 vão exercitar. */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private static final String CLIENTE = "Ana Souza";
    private static final String EMAIL = "ana@exemplo.com";

    private final PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) {
        pedidoRepository.saveAll(List.of(

                // entregue há pouco — reembolso dentro do prazo
                pedido("PED-1001", "Fone de ouvido Bluetooth", "289.90",
                        StatusPedido.ENTREGUE, 5,
                        entrega("Correios", "BR123456789BR",
                                LocalDate.now().minusDays(2), LocalDate.now().minusDays(2))),

                // entregue há 45 dias — reembolso fora do prazo
                pedido("PED-1002", "Cafeteira elétrica", "349.00",
                        StatusPedido.ENTREGUE, 45,
                        entrega("Correios", "BR987654321BR",
                                LocalDate.now().minusDays(40), LocalDate.now().minusDays(40))),

                // a caminho, previsão vencida — entrega atrasada
                pedido("PED-1003", "Teclado mecânico", "459.90",
                        StatusPedido.ENVIADO, 12,
                        entrega("Jadlog", "JD55512345",
                                LocalDate.now().minusDays(5), null)),

                // a caminho, dentro do prazo — caminho feliz
                pedido("PED-1004", "Monitor 27 polegadas", "1899.00",
                        StatusPedido.ENVIADO, 3,
                        entrega("Loggi", "LG99887766",
                                LocalDate.now().plusDays(4), null)),

                // extraviado — reembolso permitido independente do prazo
                pedido("PED-1005", "Cadeira de escritório", "1250.00",
                        StatusPedido.EXTRAVIADO, 60,
                        entrega("Jadlog", "JD11122233",
                                LocalDate.now().minusDays(50), null)),

                // nem foi pago — não há o que reembolsar
                pedido("PED-1006", "Webcam Full HD", "199.90",
                        StatusPedido.AGUARDANDO_PAGAMENTO, 1, null)
        ));

        log.info("{} pedidos de exemplo carregados", pedidoRepository.count());
    }

    private Pedido pedido(String codigo, String produto, String valor,
                          StatusPedido status, int diasAtras, Entrega entrega) {
        return Pedido.builder()
                .codigo(codigo)
                .cliente(CLIENTE)
                .emailCliente(EMAIL)
                .produto(produto)
                .valorTotal(new BigDecimal(valor))
                .status(status)
                .dataPedido(LocalDateTime.now().minusDays(diasAtras))
                .entrega(entrega)
                .build();
    }

    private Entrega entrega(String transportadora, String rastreio,
                            LocalDate previsao, LocalDate dataEntrega) {
        return new Entrega(transportadora, rastreio, previsao, dataEntrega);
    }
}
