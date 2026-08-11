package com.alura.springai.repository;

import com.alura.springai.domain.Reembolso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReembolsoRepository extends JpaRepository<Reembolso, Long> {

    boolean existsByCodigoPedido(String codigoPedido);
}
