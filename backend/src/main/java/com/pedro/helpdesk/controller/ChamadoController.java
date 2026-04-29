package com.pedro.helpdesk.controller;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.service.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    /** Filtra chamados por status: GET /chamados/status/ABERTO */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Chamado>> listarPorStatus(@PathVariable Chamado.Status status) {
        return ResponseEntity.ok(chamadoService.listarPorStatus(status));
    }

    /** Cria novo chamado (publica no Kafka automaticamente) */
    @PostMapping
    public ResponseEntity<Chamado> criar(@RequestBody Chamado chamado) {
        Chamado novo = chamadoService.salvar(chamado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    /**
     * Atualiza o status de um chamado e publica o evento no Kafka.
     * PATCH /chamados/1/status  body: { "status": "EM_ANDAMENTO" }
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Chamado> atualizarStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        Chamado.Status novoStatus = Chamado.Status.valueOf(body.get("status").toUpperCase());
        Chamado atualizado = chamadoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(atualizado);
    }
}
