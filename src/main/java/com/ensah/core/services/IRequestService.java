package com.ensah.core.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ensah.core.bo.Compte;
import com.ensah.core.bo.JournalisationEvenements;

public interface IRequestService {
	
	public String getClientIp(HttpServletRequest request);
	
	void addEvent(HttpServletRequest request, Long accountId,String criticite,String dtails,String  typeEvent);
	
	public List<JournalisationEvenements> getAllEvents();
	
	public List<JournalisationEvenements> getConnectionEvents(String eventType);

	public List<JournalisationEvenements> getActionEventsByAccount(Long account);

	
}