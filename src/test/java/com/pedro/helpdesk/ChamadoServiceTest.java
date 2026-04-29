package com.pedro.helpdesk;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.exception.ChamadoNaoEncontradoException;
import com.pedro.helpdesk.exception.TituloInvalidoException;
import com.pedro.helpdesk.kafka.ChamadoProducer;
import com.pedro.helpdesk.repository.ChamadoRepository;
import com.pedro.helpdesk.service.ChamadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChamadoService — testes unitários")
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private ChamadoProducer chamadoProducer;

    @InjectMocks
    private ChamadoService chamadoService;

    private Chamado chamado;

    @BeforeEach
    void setUp() {
        chamado = new Chamado("Sem internet", "Conexão caiu após reinicialização");
        chamado.setId(1L);
    }

    // ── salvar ────────────────────────────────────────────────────

    @Test
    @DisplayName("salvar — deve salvar e publicar no Kafka")
    void salvar_deveSalvarEPublicarNoKafka() {
        when(chamadoRepository.save(any(Chamado.class))).thenReturn(chamado);

        Chamado resultado = chamadoService.salvar(chamado);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Sem internet");
        assertThat(resultado.getStatus()).isEqualTo(Chamado.Status.ABERTO);
        verify(chamadoRepository, times(1)).save(chamado);
        verify(chamadoProducer, times(1)).publicar(chamado);
    }

    @Test
    @DisplayName("salvar — deve lançar TituloInvalidoException quando título vazio")
    void salvar_deveLancarExcecao_quandoTituloVazio() {
        chamado.setTitulo("");

        assertThatThrownBy(() -> chamadoService.salvar(chamado))
                .isInstanceOf(TituloInvalidoException.class)
                .hasMessageContaining("obrigatório");

        verify(chamadoRepository, never()).save(any());
        verify(chamadoProducer, never()).publicar(any());
    }

    @Test
    @DisplayName("salvar — deve lançar TituloInvalidoException quando título nulo")
    void salvar_deveLancarExcecao_quandoTituloNulo() {
        chamado.setTitulo(null);

        assertThatThrownBy(() -> chamadoService.salvar(chamado))
                .isInstanceOf(TituloInvalidoException.class);
    }

    @Test
    @DisplayName("salvar — deve definir status ABERTO quando não informado")
    void salvar_deveDefinirStatusAberto_quandoNaoInformado() {
        chamado.setStatus(null);
        when(chamadoRepository.save(any())).thenReturn(chamado);

        chamadoService.salvar(chamado);

        assertThat(chamado.getStatus()).isEqualTo(Chamado.Status.ABERTO);
    }

    // ── atualizarStatus ───────────────────────────────────────────

    @Test
    @DisplayName("atualizarStatus — deve atualizar e publicar no Kafka")
    void atualizarStatus_deveAtualizarEPublicarNoKafka() {
        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));
        when(chamadoRepository.save(any())).thenReturn(chamado);

        Chamado resultado = chamadoService.atualizarStatus(1L, Chamado.Status.EM_ANDAMENTO);

        assertThat(resultado.getStatus()).isEqualTo(Chamado.Status.EM_ANDAMENTO);
        verify(chamadoProducer, times(1)).publicar(chamado);
    }

    @Test
    @DisplayName("atualizarStatus — deve lançar ChamadoNaoEncontradoException quando ID inválido")
    void atualizarStatus_deveLancarExcecao_quandoIdInvalido() {
        when(chamadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chamadoService.atualizarStatus(99L, Chamado.Status.FECHADO))
                .isInstanceOf(ChamadoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    // ── listarTodos ───────────────────────────────────────────────

    @Test
    @DisplayName("listarTodos — deve retornar lista de chamados")
    void listarTodos_deveRetornarLista() {
        when(chamadoRepository.findAll()).thenReturn(List.of(chamado));

        List<Chamado> resultado = chamadoService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("Sem internet");
    }

    // ── buscarPorId ───────────────────────────────────────────────

    @Test
    @DisplayName("buscarPorId — deve retornar chamado quando ID existe")
    void buscarPorId_deveRetornarChamado() {
        when(chamadoRepository.findById(1L)).thenReturn(Optional.of(chamado));

        Chamado resultado = chamadoService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("buscarPorId — deve lançar exceção quando ID não existe")
    void buscarPorId_deveLancarExcecao_quandoIdNaoExiste() {
        when(chamadoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chamadoService.buscarPorId(99L))
                .isInstanceOf(ChamadoNaoEncontradoException.class);
    }
}
