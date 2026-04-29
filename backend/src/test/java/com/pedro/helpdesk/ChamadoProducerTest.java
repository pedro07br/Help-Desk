package com.pedro.helpdesk;

import com.pedro.helpdesk.entity.Chamado;
import com.pedro.helpdesk.kafka.ChamadoProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChamadoProducer — testes unitários")
class ChamadoProducerTest {

    @Mock
    private KafkaTemplate<String, Chamado> kafkaTemplate;

    @InjectMocks
    private ChamadoProducer chamadoProducer;

    private Chamado chamado;

    @BeforeEach
    void setUp() {
        chamado = new Chamado("Impressora não funciona", "Erro ao imprimir PDF");
        chamado.setId(1L);
    }

    @Test
    @DisplayName("publicar — deve enviar para tópico 'chamados-abertos' quando status ABERTO")
    void publicar_deveEnviarParaTopicAberto() {
        chamado.setStatus(Chamado.Status.ABERTO);
        when(kafkaTemplate.send(anyString(), anyString(), any(Chamado.class)))
                .thenReturn(new CompletableFuture<>());

        chamadoProducer.publicar(chamado);

        verify(kafkaTemplate, times(1))
                .send(eq("chamados-abertos"), eq("1"), eq(chamado));
    }

    @Test
    @DisplayName("publicar — deve enviar para tópico 'chamados-em-andamento' quando status EM_ANDAMENTO")
    void publicar_deveEnviarParaTopicEmAndamento() {
        chamado.setStatus(Chamado.Status.EM_ANDAMENTO);
        when(kafkaTemplate.send(anyString(), anyString(), any(Chamado.class)))
                .thenReturn(new CompletableFuture<>());

        chamadoProducer.publicar(chamado);

        verify(kafkaTemplate, times(1))
                .send(eq("chamados-em-andamento"), eq("1"), eq(chamado));
    }

    @Test
    @DisplayName("publicar — deve enviar para tópico 'chamados-fechados' quando status FECHADO")
    void publicar_deveEnviarParaTopicFechado() {
        chamado.setStatus(Chamado.Status.FECHADO);
        when(kafkaTemplate.send(anyString(), anyString(), any(Chamado.class)))
                .thenReturn(new CompletableFuture<>());

        chamadoProducer.publicar(chamado);

        verify(kafkaTemplate, times(1))
                .send(eq("chamados-fechados"), eq("1"), eq(chamado));
    }
}
