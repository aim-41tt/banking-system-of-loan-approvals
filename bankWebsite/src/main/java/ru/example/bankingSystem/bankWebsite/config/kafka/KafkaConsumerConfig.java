package ru.example.bankingSystem.bankWebsite.config.kafka;

import org.apache.kafka.clients.ClientRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final ConsumerFactory<String, ClientRequest> consumerFactory;

    public KafkaConsumerConfig(ConsumerFactory<String, ClientRequest> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(10); // Количество параллельных потоков, соответствующих числу партиций
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL); // Ручное подтверждение обработки
        return factory;
    }
}