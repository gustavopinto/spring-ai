package com.alura.springai.repository;

import com.alura.springai.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /** Evita abrir dois chamados abertos para o mesmo pedido. */
    Optional<Ticket> findFirstByCodigoPedidoAndStatus(String codigoPedido, String status);
}
