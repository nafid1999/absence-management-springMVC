package com.ensah.core.services;

import java.util.Date;
import java.util.List;

import com.ensah.core.bo.Compte;
import com.ensah.core.bo.JournalisationEvenements;
import com.ensah.core.bo.Role;
import com.ensah.core.bo.Utilisateur;
import com.ensah.core.utils.ExcelExporter;

public interface ICompteService {
	//ABOUT ACCOUNT
	public  Compte getAccountById(Long id);
	
	public  void update( Compte id);  
	
	public Compte findByUsername(String username);
	
	public List<Compte> getAllAccounts();
	
	public  String renitialisePassword(Long idCompte);


    //ABOUT ROLES
	public List<Role> getAllRoles();
	
	public Role getRoleById(Long id_role);
	
	public void updateRoleUser(Long idRole,Long idAccount);

    //LE VROUILLAGE DU COMPTE
	public void increaseFailedAttempts(Compte user);
	 
	public void resetFailedAttempts(String email) ;
	     
	public void lock(Compte user) ;
	     
	public boolean unlockWhenTimeExpired(Compte user) ;
	

	
    public String createUser(Long idRole, Long idPerson);
	
	public ExcelExporter prepareCompteExport(List<Compte> comptes) ;

	
	
}
