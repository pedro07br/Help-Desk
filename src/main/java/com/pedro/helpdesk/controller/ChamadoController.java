package com.pedro.helpdesk.controller;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.service.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {
    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> listarTodos() {
        List<Chamado> lista = chamadoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarPorId(@PathVariable Long id) {
        Chamado chamado = chamadoService.buscarPorId(id);
        return ResponseEntity.ok(chamado);
    }

    @PostMapping
    public ResponseEntity<Chamado> criar(@RequestBody Chamado chamado) {
        Chamado novoChamado = chamadoService.salvar(chamado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoChamado);
    }
}
