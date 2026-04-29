package com.pedro.helpdesk;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.kafka.ChamadoConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChamadoConsumer — testes unitários")
class ChamadoConsumerTest {

    @InjectMocks
    private ChamadoConsumer chamadoConsumer;

    private Chamado chamado;

    @BeforeEach
    void setUp() {
        chamado = new Chamado("Sistema fora do ar", "Aplicação não responde");
        chamado.setId(2L);
    }

    @Test
    @DisplayName("onChamadoAberto — deve processar mensagem sem lançar exceção")
    void onChamadoAberto_deveProcessarSemErro() {
        chamado.setStatus(Chamado.Status.ABERTO);
        assertDoesNotThrow(() -> chamadoConsumer.onChamadoAberto(chamado, 0, 0L));
    }

    @Test
    @DisplayName("onChamadoEmAndamento — deve processar mensagem sem lançar exceção")
    void onChamadoEmAndamento_deveProcessarSemErro() {
        chamado.setStatus(Chamado.Status.EM_ANDAMENTO);
        assertDoesNotThrow(() -> chamadoConsumer.onChamadoEmAndamento(chamado, 0, 0L));
    }

    @Test
    @DisplayName("onChamadoFechado — deve processar mensagem sem lançar exceção")
    void onChamadoFechado_deveProcessarSemErro() {
        chamado.setStatus(Chamado.Status.FECHADO);
        assertDoesNotThrow(() -> chamadoConsumer.onChamadoFechado(chamado, 0, 0L));
    }
}
