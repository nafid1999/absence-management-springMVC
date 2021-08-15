package com.ensah.core.services.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ensah.core.bo.Compte;
import com.ensah.core.bo.JournalisationEvenements;
import com.ensah.core.dao.ICompteDao;
import com.ensah.core.dao.IEventDao;
import  com.ensah.core.services.*;


@Service
@Transactional
public class RequestServiceImpl implements IRequestService  {

	private final String LOCALHOST_IPV4 = "127.0.0.1";
	private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	
	@Autowired
	private IEventDao  eventDao;
	@Autowired
	private ICompteDao userDao;
	
	@Override
	public String getClientIp(HttpServletRequest request) {

		String ipAddress = request.getHeader("X-Forwarded-For");
		if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		

		if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
				try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					ipAddress = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		return ipAddress;
	}

	
	


	@Override
	public void addEvent(HttpServletRequest request, Long accountId, String criticite, String details,
			String typeEvent) {
		
       JournalisationEvenements event=new JournalisationEvenements();
		
		event.setCompte(userDao.findById(accountId));
		event.setAdresseIP(getClientIp( request));
		event.setDateHeure(new Date());
		event.setCriticite(criticite);
		event.setTypeEvenement(typeEvent);
		event.setDetails(details);

		eventDao.create(event);
		
	}


	@Override
	public List<JournalisationEvenements> getAllEvents() {
		
		return eventDao.getAll();
	}





	@Override
	public List<JournalisationEvenements> getConnectionEvents(String eventType) {
		// TODO Auto-generated method stub
		return eventDao.getEntityByColValue("JournalisationEvenements", "typeEvenement", eventType);
	}
	
	public List<JournalisationEvenements> getActionEventsByAccount(Long accountID) {
		
		if(eventDao.getEntityByColValue("JournalisationEvenements", "idCompte",String.valueOf(accountID)).isEmpty())
			return null;
		
		return 	eventDao.getEntityByColValue("JournalisationEvenements", "idCompte",String.valueOf(accountID));
	}



}





	

	
	
	
