package ru.example.bankingSystem.LoanAssessmentSystem.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = "clientTaskExecutor")
	public Executor clientTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(512);
		executor.setMaxPoolSize(1024);
		executor.setQueueCapacity(250000);
		executor.setThreadNamePrefix("ClientTaskExecutor-");
		executor.initialize();
		return executor;
	}
}