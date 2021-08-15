package com.ensah.core.services.impl;

import java.util.Date;
import java.util.List;

import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ensah.core.bo.Utilisateur;
import com.ensah.core.bo.Role;
import com.ensah.core.bo.Compte;
import com.ensah.core.bo.JournalisationEvenements;
import com.ensah.core.dao.IUtilisateurDao;
import com.ensah.core.dao.IRoleDao;
import com.ensah.core.dao.ICompteDao;
import com.ensah.core.services.ICompteService;
import com.ensah.core.utils.ExcelExporter;

@Service
@Transactional
public class CompteServiceImpl implements ICompteService {
	
	public static final int MAX_FAILED_ATTEMPTS = 3;
    
    private static final long LOCK_TIME_DURATION =60*2;   //24 * 60 * 60 * 1000; // 24 hours
     

	@Autowired
	private ICompteDao userDao;

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IUtilisateurDao personDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<Role> getAllRoles() {
		return roleDao.getAll();
	}
	
	public Role getRoleById(Long id_role) {
		return roleDao.findById(id_role);
	}
	

	public List<Compte> getAllAccounts() {
		return userDao.getAll();
	}
	public String createUser(Long idRole, Long idPerson) {

		// récupérer la personne de la base de données
		Utilisateur person = personDao.findById(idPerson);

		// Créer le compte
		Compte userAccount = new Compte();

		// determiner la personne
		userAccount.setProprietaire(person);

		

		// Affecter le role
		userAccount.setRole(roleDao.findById(idRole));

		// génrer le mot de passe aléatoirement
		String generatedPass = generatePassayPassword();


		// hachage du mot de passe + gain de sel
		String encodedPass = passwordEncoder.encode(generatedPass);
		

		// affecter ce mot de passe
		userAccount.setPassword(encodedPass);

		
		
		//On construit un login de type "nom+prenom " s'il est dispo
		String login = person.getNom() + person.getPrenom();

		List<Compte> accounts = userDao.getEntityByColValue("Compte", "login", login);

		if (accounts == null || accounts.size() == 0) {
			
			userAccount.setLogin(login);
			
			//Créer le compte
			userDao.create(userAccount);
			return generatedPass;
		}

		int i = 0;

		// sinon, on cherche un login de type nom+prenom+"_"+ entier
		while (true) {

			login = person.getNom() + person.getPrenom() + "_" + i;
			accounts = userDao.getEntityByColValue("Compte", "login", login);
			if (accounts == null || accounts.size() == 0) {
				userAccount.setLogin(login);
				
				//Créer le compte
				userDao.create(userAccount);
				return generatedPass;
			}

			i++;
		}
	}

	
	public ExcelExporter prepareCompteExport(List<Compte> comptes) {
		String[] columnNames = new String[] { "Login", "Rôle", "Nom & Prénom" };
		String[][] data = new String[comptes.size()][3];

		int i = 0;
		for (Compte u : comptes) {
			data[i][0] = u.getLogin();
			data[i][1] = u.getRole().getNomRole();
			data[i][2] = u.getProprietaire().getNom() +" "+ u.getProprietaire().getPrenom();
			i++;
		}

		return new ExcelExporter(columnNames, data, "comptes");

	}
	
	//génère le mot de passe. Il se base sur Passay
	public String generatePassayPassword() {
		CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generatePassword(10, digits);

		return password;
	}



	@Override
	public Compte getAccountById(Long id) {
		
		return userDao.findById(id);
	}



	@Override
	public void  update(Compte account) {
		
		userDao.update(account);
	}



	@Override
	public String renitialisePassword(Long idCompte) {
		     // récupérer le compte
				Compte compte = userDao.findById(idCompte);
               //generate password
				String generatedPass = generatePassayPassword();

				// hachage du mot de passe + gain de sel
				String encodedPass = passwordEncoder.encode(generatedPass);
             
				//affecter le password
				compte.setPassword(encodedPass);
                //update account
				userDao.update(compte);
				
				return generatedPass;
		
	}



	@Override
	public void updateRoleUser(Long idRole, Long idAccount) {

		Compte account = userDao.findById(idAccount);
	
		account.setRole(roleDao.findById(idRole));
		
		userDao.update(account);

	}
	
	 public void increaseFailedAttempts(Compte user) {
	        int newFailAttempts = user.getFailedAttempt() + 1;
	        userDao.updateFailedAttempts(newFailAttempts, user.getLogin());
	    }
	     
	    public void resetFailedAttempts(String email) {
	    	userDao.updateFailedAttempts(0, email);
	    }
	     
	    public void lock(Compte user) {
	        user.setAccountNonLocked(false);
	        user.setLockTime(new Date());
 
	        userDao.update(user);
	    }
	     
	    public boolean unlockWhenTimeExpired(Compte user) {
	        long lockTimeInMillis = user.getLockTime().getTime();
	        long currentTimeInMillis = System.currentTimeMillis();
	         
	        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
	            user.setAccountNonLocked(true);
	            user.setLockTime(null);
	            user.setFailedAttempt(0);
	             
	            userDao.update(user);
	             
	            return true;
	        }
	         
	        return false;
	    }



		@Override
		public Compte findByUsername(String username) {
			List<Compte> users = userDao.getEntityByColValue("Compte", "login", username);
			return users.size() != 0
					? users.get(0)
					: null;
		}

		
	}

	


