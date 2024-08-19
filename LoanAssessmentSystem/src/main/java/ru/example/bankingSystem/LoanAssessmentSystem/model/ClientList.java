package ru.example.bankingSystem.LoanAssessmentSystem.model;

import java.util.List;

public class ClientList {
	List<Client> clients;

	public ClientList() {}
	
	public ClientList(List<Client> clients) {
		this.clients = clients;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	
}
