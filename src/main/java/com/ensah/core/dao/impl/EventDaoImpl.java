package com.ensah.core.dao.impl;


import org.springframework.stereotype.Repository;

import com.ensah.core.bo.JournalisationEvenements;
import com.ensah.core.bo.Role;
import com.ensah.core.dao.IEventDao;
import com.ensah.genericdao.EntityNotFoundException;
import com.ensah.genericdao.HibernateSpringGenericDaoImpl;
@Repository
public class EventDaoImpl extends HibernateSpringGenericDaoImpl<JournalisationEvenements, Long> implements IEventDao {

	public EventDaoImpl() {
		super(JournalisationEvenements.class);
	}

		

	

}
