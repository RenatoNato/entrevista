package com.financeiro.controller;

import com.financeiro.model.Transferencia;
import com.financeiro.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

    @PostMapping("/agendar")
    public ResponseEntity<Transferencia> agendarTransferencia(@RequestBody Transferencia transferencia) {
        return ResponseEntity.ok(transferenciaService.agendarTransferencia(transferencia));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Transferencia>> listarAgendamentos() {
        return ResponseEntity.ok(transferenciaService.listarAgendamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> buscarTransferenciaPorId(@PathVariable Long id) {
        Optional<Transferencia> transferencia = transferenciaService.buscarPorId(id);
        return transferencia.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Transferencia> atualizarTransferencia(@PathVariable Long id, @RequestBody Transferencia transferencia) {
        return ResponseEntity.ok(transferenciaService.atualizarTransferencia(id, transferencia));
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirTransferencia(@PathVariable Long id) {
        transferenciaService.excluirTransferencia(id);
        return ResponseEntity.noContent().build();
    }
}
