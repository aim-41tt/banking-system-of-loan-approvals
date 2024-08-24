package ru.example.bankingSystem.bankWebsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BankWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankWebsiteApplication.class, args);
	}

}
