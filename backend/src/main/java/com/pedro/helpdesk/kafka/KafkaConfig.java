package com.pedro.helpdesk.kafka;

import com.pedro.helpdesk.entity.Chamado;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    // ── Tópicos ──────────────────────────────────────────────────
    @Bean
    public NewTopic topicAberto() {
        return TopicBuilder.name("chamados-abertos").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic topicEmAndamento() {
        return TopicBuilder.name("chamados-em-andamento").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic topicFechado() {
        return TopicBuilder.name("chamados-fechados").partitions(3).replicas(1).build();
    }

    // ── KafkaTemplate (Spring Boot injeta o ProducerFactory) ─────
    @Bean
    public KafkaTemplate<String, Chamado> kafkaTemplate(ProducerFactory<String, Chamado> pf) {
        return new KafkaTemplate<>(pf);
    }

    // ── ContainerFactory (Spring Boot injeta o ConsumerFactory) ──
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Chamado> kafkaListenerContainerFactory(
            ConsumerFactory<String, Chamado> cf) {
        ConcurrentKafkaListenerContainerFactory<String, Chamado> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        return factory;
    }
}