/*
 * Projeto base · Acesso a dados de Pedido.
 *
 * Nenhum código de IA passa por aqui. O agente sempre fala com o Service.
 */
package com.alura.springai.repository;

import com.alura.springai.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByCodigo(String codigo);

    List<Pedido> findByEmailCliente(String emailCliente);
}
