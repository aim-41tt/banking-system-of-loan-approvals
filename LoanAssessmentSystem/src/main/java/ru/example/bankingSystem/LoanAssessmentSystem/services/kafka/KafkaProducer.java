package ru.example.bankingSystem.LoanAssessmentSystem.services.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String value, String key) {
		String topic = "results_loan_approval_to_client";

		kafkaTemplate.send(topic, key, value);
	}
}
