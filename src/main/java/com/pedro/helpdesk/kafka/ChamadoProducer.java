package com.pedro.helpdesk.kafka;

import com.pedro.helpdesk.entity.Chamado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Publica eventos de Chamado nos tópicos Kafka conforme o status.
 * É chamado pelo ChamadoService após cada operação relevante.
 */
@Service
public class ChamadoProducer {

    private static final Logger log = LoggerFactory.getLogger(ChamadoProducer.class);

    public static final String TOPIC_ABERTO       = "chamados-abertos";
    public static final String TOPIC_EM_ANDAMENTO = "chamados-em-andamento";
    public static final String TOPIC_FECHADO      = "chamados-fechados";

    private final KafkaTemplate<String, Chamado> kafkaTemplate;

    public ChamadoProducer(KafkaTemplate<String, Chamado> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publica o chamado no tópico correspondente ao seu status atual.
     */
    public void publicar(Chamado chamado) {
        String topico = resolverTopico(chamado.getStatus());

        kafkaTemplate.send(topico, String.valueOf(chamado.getId()), chamado)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("❌ Erro ao publicar chamado id={} no tópico '{}': {}",
                            chamado.getId(), topico, ex.getMessage());
                } else {
                    log.info("✅ Chamado id={} publicado → tópico='{}' | partição={} | offset={}",
                            chamado.getId(), topico,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });
    }

    private String resolverTopico(Chamado.Status status) {
        return switch (status) {
            case ABERTO       -> TOPIC_ABERTO;
            case EM_ANDAMENTO -> TOPIC_EM_ANDAMENTO;
            case FECHADO      -> TOPIC_FECHADO;
        };
    }
}
