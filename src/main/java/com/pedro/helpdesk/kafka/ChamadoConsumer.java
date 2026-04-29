package com.pedro.helpdesk.kafka;

import com.pedro.helpdesk.entity.Chamado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ChamadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(ChamadoConsumer.class);

    @KafkaListener(topics = "chamados-abertos", groupId = "grupo-helpdesk")
    public void onChamadoAberto(
            @Payload Chamado chamado,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int particao,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("[ABERTO] id={} | título='{}' | partição={} | offset={}",
                chamado.getId(), chamado.getTitulo(), particao, offset);
        System.out.printf("%nNovo chamado ABERTO: [%d] %s%n", chamado.getId(), chamado.getTitulo());
    }

    @KafkaListener(topics = "chamados-em-andamento", groupId = "grupo-helpdesk")
    public void onChamadoEmAndamento(
            @Payload Chamado chamado,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int particao,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("[EM_ANDAMENTO] id={} | título='{}' | partição={} | offset={}",
                chamado.getId(), chamado.getTitulo(), particao, offset);
        System.out.printf("%nChamado EM ANDAMENTO: [%d] %s%n", chamado.getId(), chamado.getTitulo());
    }

    @KafkaListener(topics = "chamados-fechados", groupId = "grupo-helpdesk")
    public void onChamadoFechado(
            @Payload Chamado chamado,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int particao,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("[FECHADO] id={} | título='{}' | partição={} | offset={}",
                chamado.getId(), chamado.getTitulo(), particao, offset);
        System.out.printf("%nChamado FECHADO: [%d] %s%n", chamado.getId(), chamado.getTitulo());
    }

}