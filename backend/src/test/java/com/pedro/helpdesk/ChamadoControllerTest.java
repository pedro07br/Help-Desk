package com.pedro.helpdesk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedro.helpdesk.controller.ChamadoController;
import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.exception.ChamadoNaoEncontradoException;
import com.pedro.helpdesk.service.ChamadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChamadoController.class)
@DisplayName("ChamadoController — testes de integração (MockMvc)")
class ChamadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChamadoService chamadoService;

    private Chamado chamado;

    @BeforeEach
    void setUp() {
        chamado = new Chamado("Teclado não funciona", "Teclas travadas");
        chamado.setId(1L);
        chamado.setStatus(Chamado.Status.ABERTO);
    }

    // ── GET /chamados ─────────────────────────────────────────────

    @Test
    @DisplayName("GET /chamados — deve retornar lista de chamados com status 200")
    void listarTodos_deveRetornar200() throws Exception {
        when(chamadoService.listarTodos()).thenReturn(List.of(chamado));

        mockMvc.perform(get("/chamados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Teclado não funciona"))
                .andExpect(jsonPath("$[0].status").value("ABERTO"));
    }

    // ── GET /chamados/{id} ────────────────────────────────────────

    @Test
    @DisplayName("GET /chamados/1 — deve retornar chamado com status 200")
    void buscarPorId_deveRetornar200() throws Exception {
        when(chamadoService.buscarPorId(1L)).thenReturn(chamado);

        mockMvc.perform(get("/chamados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Teclado não funciona"));
    }

    @Test
    @DisplayName("GET /chamados/99 — deve retornar 404 quando não encontrado")
    void buscarPorId_deveRetornar404_quandoNaoEncontrado() throws Exception {
        when(chamadoService.buscarPorId(99L))
                .thenThrow(new ChamadoNaoEncontradoException("ID 99 não encontrado!"));

        mockMvc.perform(get("/chamados/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("ID 99 não encontrado!"));
    }

    // ── GET /chamados/status/{status} ─────────────────────────────

    @Test
    @DisplayName("GET /chamados/status/ABERTO — deve retornar chamados filtrados")
    void listarPorStatus_deveRetornarChamadosFiltrados() throws Exception {
        when(chamadoService.listarPorStatus(Chamado.Status.ABERTO)).thenReturn(List.of(chamado));

        mockMvc.perform(get("/chamados/status/ABERTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ABERTO"));
    }

    // ── POST /chamados ────────────────────────────────────────────

    @Test
    @DisplayName("POST /chamados — deve criar chamado e retornar 201")
    void criar_deveRetornar201() throws Exception {
        when(chamadoService.salvar(any(Chamado.class))).thenReturn(chamado);

        mockMvc.perform(post("/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Teclado não funciona"))
                .andExpect(jsonPath("$.status").value("ABERTO"));
    }

    // ── PATCH /chamados/{id}/status ───────────────────────────────

    @Test
    @DisplayName("PATCH /chamados/1/status — deve atualizar status e retornar 200")
    void atualizarStatus_deveRetornar200() throws Exception {
        chamado.setStatus(Chamado.Status.EM_ANDAMENTO);
        when(chamadoService.atualizarStatus(eq(1L), eq(Chamado.Status.EM_ANDAMENTO)))
                .thenReturn(chamado);

        mockMvc.perform(patch("/chamados/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "EM_ANDAMENTO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));
    }
}
