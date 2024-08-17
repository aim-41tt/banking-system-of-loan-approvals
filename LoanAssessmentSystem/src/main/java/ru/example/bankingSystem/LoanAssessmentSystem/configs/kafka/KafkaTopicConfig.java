package ru.example.bankingSystem.LoanAssessmentSystem.configs.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {


    // Создание топика
    @Bean
    public NewTopic clientRequestTopic() {
        return TopicBuilder.name("client_requests") // Название топика
                .partitions(10) // Количество партиций
                .replicas(1) // Количество реплик (зависит от доступности и отказоустойчивости)
                .build();
    }

}
