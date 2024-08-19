package ru.example.bankingSystem.LoanAssessmentSystem.services;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.example.bankingSystem.LoanAssessmentSystem.model.Client;

@Service
public class LoanApprovalService {

    // Параметры для принятия решения о выдаче кредита
    private static final int MIN_AGE = 21;
    private static final int MAX_AGE = 65;
    private static final int MAX_CREDITS = 2;
    private static final int MAX_DELAYS = 1;
    private static final double INCOME_TO_LOAN_RATIO = 0.5;
    
    @Async("clientTaskExecutor")
    public void processClient(Client client) {
        boolean isApproved = evaluateClient(client);

        if (isApproved) {
            System.out.println("Кредит одобрен для клиента: " + client.getFullName());
        } else {
            System.out.println("Кредит отклонен для клиента: " + client.getFullName());
        }
    }
    
    public boolean evaluateClient(Client client) {
        return isAgeValid(client) &&
               hasAcceptableNumberOfCredits(client) &&
               hasNoDelays(client) &&
               isIncomeSufficient(client);
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

