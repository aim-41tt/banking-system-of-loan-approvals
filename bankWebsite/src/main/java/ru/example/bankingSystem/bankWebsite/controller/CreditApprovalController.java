package ru.example.bankingSystem.bankWebsite.controller;

import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import ru.example.bankingSystem.bankWebsite.model.Client;

@Controller
public class CreditApprovalController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public CreditApprovalController(KafkaTemplate<String, String> kafkaTemplate, SimpMessagingTemplate messagingTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        return "creditForm";
    }

    @PostMapping("/api/credit/apply")
    @ResponseBody
    public Mono<String> applyForCredit(@RequestBody Client client) {
        String clientData = convertClientToJson(client);
        String key = UUID.randomUUID().toString();

        return Mono.fromRunnable(() -> kafkaTemplate.send("client_requests", key, clientData))
                   .then(Mono.just("Заявка отправлена на рассмотрение. Ожидайте ответ."));
    }

    private String convertClientToJson(Client client) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            return objectMapper.writeValueAsString(client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting client to JSON", e);
        }
    }

    @KafkaListener(topics = "results_loan_approval_to_client", groupId = "client_requests_group", containerFactory = "kafkaListenerContainerFactory")
    public void listenLoanApproval(ConsumerRecord<String, String> record, Acknowledgment ack) {
        System.out.println("Received loan approval result: " + record.value());
        
        // Отправляем сообщение клиенту через WebSocket
        messagingTemplate.convertAndSend("/topic/loanApproval", record.value());
        
        ack.acknowledge();
    }
}
