package com.alura.springai.service;

import com.alura.springai.dto.PedidoDTO;
import com.alura.springai.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Este service não sabe que existe IA. A partir da Aula 2 o agente vira apenas
 * mais um cliente dele, igual ao PedidoController.
 */
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional(readOnly = true)
    public Optional<PedidoDTO> buscarPorCodigo(String codigo) {
        return pedidoRepository.findByCodigo(codigo).map(PedidoDTO::de);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPorCliente(String emailCliente) {
        return pedidoRepository.findByEmailCliente(emailCliente)
                .stream()
                .map(PedidoDTO::de)
                .toList();
    }
}
