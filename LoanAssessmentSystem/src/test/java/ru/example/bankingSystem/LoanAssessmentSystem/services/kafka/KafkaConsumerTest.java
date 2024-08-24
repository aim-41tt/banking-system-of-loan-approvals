package ru.example.bankingSystem.LoanAssessmentSystem.services.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.example.bankingSystem.LoanAssessmentSystem.model.Client;
import ru.example.bankingSystem.LoanAssessmentSystem.model.ClientList;
import ru.example.bankingSystem.LoanAssessmentSystem.services.LoanApprovalService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = { "client_requests" }, brokerProperties = {
		"listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class KafkaConsumerTest {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@MockBean
	private LoanApprovalService loanApprovalService;

	@BeforeEach
	void setUp() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("client_requests_group", "true",
				embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps,
				new StringDeserializer(), new StringDeserializer());

		ContainerProperties containerProperties = new ContainerProperties("client_requests");
		KafkaMessageListenerContainer<String, String> container = new KafkaMessageListenerContainer<>(consumerFactory,
				containerProperties);
		container.setupMessageListener((MessageListener<String, String>) record -> {});
		container.start();
	}

	@AfterEach
	void tearDown() {
		// Очистка ресурсов
	}

	@Test
	void testKafkaListenerClientList() throws Exception {
		ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
		
		Client client1 = new Client("Иванов Иван Иванович", LocalDate.of(1985, 3, 12), 3, 1, 80000L, 5000L);
		Client client2 = new Client("Иванов Иван Иванович", LocalDate.of(3000, 3, 12), 3, 1, 80000L, 5000L);
	

		ClientList clientList = new ClientList(Arrays.asList(client1, client2));
		String message = mapper.writeValueAsString(clientList);

		// Мокаем вызов сервиса
		doNothing().when(loanApprovalService).processClient(any(Client.class), any(String.class));

		// Отправка сообщения в Kafka
		kafkaTemplate.send("client_requests", "key1", message);

		// Ожидание получения и обработки сообщения
		Thread.sleep(2000);

		// Проверка, что service.processClient был вызван два раза (для каждого клиента в списке)
		verify(loanApprovalService, times(2)).processClient(any(Client.class), any(String.class));
	}

	@Test
	void testKafkaListenerSingleClient() throws Exception {
		ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();;

		Client client = new Client("Иванов Иван Иванович", LocalDate.of(1985, 3, 12), 3, 1, 80000L, 5000L);
		String message = mapper.writeValueAsString(client);

		// Мокаем вызов сервиса
		doNothing().when(loanApprovalService).processClient(any(Client.class), any(String.class));

		kafkaTemplate.send("client_requests", "key2", message);

		Thread.sleep(2000);

		// Проверка, что service.processClient был вызван один раз
		verify(loanApprovalService, times(1)).processClient(any(Client.class), any(String.class));
	}
}
