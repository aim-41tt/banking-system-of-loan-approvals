package ru.example.bankingSystem.LoanAssessmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class LoanAssessmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanAssessmentSystemApplication.class, args);
	}

}
