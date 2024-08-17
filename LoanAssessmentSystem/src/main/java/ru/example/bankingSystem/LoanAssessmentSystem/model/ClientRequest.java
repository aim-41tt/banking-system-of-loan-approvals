package ru.example.bankingSystem.LoanAssessmentSystem.model;

import java.time.LocalDate;

public class ClientRequest {

	private String fullName; // ФИО
	private LocalDate dateOfBirth; // Дата рождения
	private int numberOfCredits; // Количество кредитов
	private int numberOfDelays; // Количество просрочек
	private Long monthlyIncome; // Ежемесячный доход
	private Long requestedLoanAmount; // Запрашиваемая 

	/*
	  { 
	  "fullName": "Иванов Иван Иванович",
	  "dateOfBirth": "1985-03-12",
	  "numberOfCredits": 3, 
	  "numberOfDelays": 1,
	  "monthlyIncome": 80000,
	  "requestedLoanAmount": 500000 
	  }
	 */
		
	public ClientRequest() {
	}

	public ClientRequest(String fullName, LocalDate dateOfBirth, int numberOfCredits, int numberOfDelays,
			Long monthlyIncome, Long requestedLoanAmount) {
		this.fullName = fullName;
		this.dateOfBirth = dateOfBirth;
		this.numberOfCredits = numberOfCredits;
		this.numberOfDelays = numberOfDelays;
		this.monthlyIncome = monthlyIncome;
		this.requestedLoanAmount = requestedLoanAmount;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getNumberOfCredits() {
		return numberOfCredits;
	}

	public void setNumberOfCredits(int numberOfCredits) {
		this.numberOfCredits = numberOfCredits;
	}

	public int getNumberOfDelays() {
		return numberOfDelays;
	}

	public void setNumberOfDelays(int numberOfDelays) {
		this.numberOfDelays = numberOfDelays;
	}

	public Long getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(Long monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public Long getRequestedLoanAmount() {
		return requestedLoanAmount;
	}

	public void setRequestedLoanAmount(Long requestedLoanAmount) {
		this.requestedLoanAmount = requestedLoanAmount;
	}

	@Override
	public String toString() {
		return "ClientRequest [fullName=" + fullName + ", dateOfBirth=" + dateOfBirth + ", numberOfCredits="
				+ numberOfCredits + ", numberOfDelays=" + numberOfDelays + ", monthlyIncome=" + monthlyIncome
				+ ", requestedLoanAmount=" + requestedLoanAmount + "]";
	}
	
	
}
