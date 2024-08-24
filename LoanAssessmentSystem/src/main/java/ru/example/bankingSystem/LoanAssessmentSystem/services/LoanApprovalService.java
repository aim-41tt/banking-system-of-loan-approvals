package ru.example.bankingSystem.LoanAssessmentSystem.services;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.example.bankingSystem.LoanAssessmentSystem.model.Client;
import ru.example.bankingSystem.LoanAssessmentSystem.services.kafka.KafkaProducer;

@Service
public class LoanApprovalService {

	// Параметры для принятия решения о выдаче кредита
	private static final int MIN_AGE = 21;
	private static final int MAX_AGE = 65;
	private static final int MAX_CREDITS = 2;
	private static final int MAX_DELAYS = 1;
	private static final double INCOME_TO_LOAN_RATIO = 0.5;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Async("clientTaskExecutor")
	public void processClient(Client client, String key) {
		boolean isApproved = evaluateClient(client);
		StringBuilder sb = new StringBuilder();
		
		if (isApproved) {
			sb.append("Кредит одобрен для клиента: ");
			sb.append(client.getFullName()+ ".");
			
			kafkaProducer.sendMessage(sb.toString(), key);
		} else {
			sb.append("Кредит отклонен для клиента: ");
			sb.append(client.getFullName());
			sb.append(" отказ по:");
			sb.append(client.getReasonRefusal() + ".");
			
			kafkaProducer.sendMessage(sb.toString(), key);
		}
	}

	private boolean evaluateClient(Client client) {		
		boolean approvalFlag = true;
		
		if(!isAgeValid(client)) {
			client.addReasonRefusal("Неподходящий возраст");
			approvalFlag = false;
		}
		
		if (!hasAcceptableNumberOfCredits(client)) {
			client.addReasonRefusal("Много открытых кредитов");
			approvalFlag = false;
		}
		
		if (!hasNoDelays(client)) {
			client.addReasonRefusal("Просрочки платежей");
			approvalFlag = false;
		}
		
		if (!isIncomeSufficient(client)) {
			client.addReasonRefusal("Доход недостаточный");
			approvalFlag = false;
		}
		
		return approvalFlag;
	}

	private boolean isAgeValid(Client client) {
		int age = Period.between(client.getDateOfBirth(), LocalDate.now()).getYears();
		return age >= MIN_AGE && age <= MAX_AGE;
	}

	private boolean hasAcceptableNumberOfCredits(Client client) {
		return client.getNumberOfCredits() <= MAX_CREDITS;
	}

	private boolean hasNoDelays(Client client) {
		return client.getNumberOfDelays() <= MAX_DELAYS;
	}

	private boolean isIncomeSufficient(Client client) {
		long annualIncome = client.getMonthlyIncome() * 12;
		return client.getRequestedLoanAmount() <= annualIncome * INCOME_TO_LOAN_RATIO;
	}
}
