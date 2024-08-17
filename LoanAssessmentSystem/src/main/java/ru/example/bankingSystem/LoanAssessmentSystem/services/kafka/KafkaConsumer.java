package ru.example.bankingSystem.LoanAssessmentSystem.services.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.example.bankingSystem.LoanAssessmentSystem.model.ClientRequest;

@Service
public class KafkaConsumer {

	@KafkaListener(topics = "client_requests", groupId = "client_requests_group", containerFactory = "kafkaListenerContainerFactory")
	public void listen(String clientRequestMessages, Acknowledgment ack) {
		try {
			// Обработка сообщения
			System.out.println("client request: " + clientRequestMessages);

			// Логика обработки
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			
			ClientRequest clientRequest = mapper.readValue(clientRequestMessages, ClientRequest.class);

			System.out.println("Processing client request: " + clientRequest.toString());
			
			// Подтверждение успешной обработки
			ack.acknowledge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
