package com.alura.springai.controller;

import com.alura.springai.dto.PedidoDTO;
import com.alura.springai.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping("/{codigo}")
    public ResponseEntity<PedidoDTO> buscar(@PathVariable String codigo) {
        return pedidoService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(@RequestParam String email) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(email));
    }
}
