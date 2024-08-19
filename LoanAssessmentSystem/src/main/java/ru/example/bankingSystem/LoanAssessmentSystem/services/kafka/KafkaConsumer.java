package ru.example.bankingSystem.LoanAssessmentSystem.services.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import ru.example.bankingSystem.LoanAssessmentSystem.model.Client;
import ru.example.bankingSystem.LoanAssessmentSystem.model.ClientList;
import ru.example.bankingSystem.LoanAssessmentSystem.services.LoanApprovalService;

@Service
public class KafkaConsumer {

	@Autowired
	private LoanApprovalService loanApprovalService;

	@KafkaListener(topics = "client_requests", groupId = "client_requests_group", containerFactory = "kafkaListenerContainerFactory")
	public void listen(String clientRequestMessages, Acknowledgment ack) {
		try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            try {
                // Пытаемся десериализовать как список клиентов
                ClientList clientList = mapper.readValue(clientRequestMessages, ClientList.class);
                for (Client client : clientList.getClients()) {
                    loanApprovalService.processClient(client);
                }
            } catch (UnrecognizedPropertyException e) {
                // Если десериализация как списка не удалась, пробуем десериализовать как одного клиента
                Client client = mapper.readValue(clientRequestMessages, Client.class);
                loanApprovalService.processClient(client);
            }

            System.out.println("Processing client request: " + clientRequestMessages);

            ack.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}