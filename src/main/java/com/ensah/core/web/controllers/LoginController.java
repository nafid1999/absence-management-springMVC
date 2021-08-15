package com.ensah.core.web.controllers;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.web.bind.annotation.GetMapping;

import com.ensah.core.bo.Compte;
import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.UserPrincipal;
import com.ensah.core.bo.Utilisateur;
import com.ensah.core.services.IRequestService;
import com.ensah.core.web.models.UserAndAccountInfos;


@Controller

public class LoginController {
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private IRequestService requestService;

	/**
	 * Récupère les données de l'utilisateur connecté du contexte de securité et le
	 * stocke dans un objet personnalisé à enregistrer dans la session http
	 * @return
	 */
	private UserAndAccountInfos getUserAccount() {

		// On vérifie si les infors de l'utilisateur sont déjà dans la session
		UserAndAccountInfos userInfo = (UserAndAccountInfos) httpSession.getAttribute("userInfo");

		if (userInfo == null) {

			// On obtient l'objet representant le compte connecté (Objet UserPrincipal
			// implémentant UserDetails)
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			// On cast vers notre objet UserPrincipal
			Compte userAccount = ((UserPrincipal) principal).getUser();

			Utilisateur u = userAccount.getProprietaire();

			userInfo = new UserAndAccountInfos(u.getIdUtilisateur(), userAccount.getIdCompte(), userAccount.getLogin(),
					u.getNom(), u.getPrenom(), u.getEmail());

			// On le stocke dans la session
			httpSession.setAttribute("userInfo", userInfo);
		}

		return userInfo;

	}

	@GetMapping("/showMyLoginPage")
	public String showLoginForm(Model model) {
		
		return "loginForm";
	}

	@GetMapping("/access-denied")
	public String showAccessDenied() {

		return "access-denied";

	}

	@GetMapping("/student/showHome")
	public String showStudentHomePage(HttpServletRequest request) {
		Long accountId = getUserAccount().getIdCompte();
		String login =  getUserAccount().getLogin();
		requestService.addEvent(request, accountId, "TRACE", login, "Connexion");
		

		return "student/userHomePage";

	}
	//TEST
	@GetMapping("/student/service")
	public String showStudentService(HttpServletRequest request) {
		Long accountId = getUserAccount().getIdCompte();
		String login =  getUserAccount().getLogin();
		requestService.addEvent(request, accountId, "TRACE", request.getRequestURL().toString(), "ACTION");
		

		return "student/test";
	}
	
	@GetMapping("/prof/showHome")
	public String showProfHomePage(HttpServletRequest request) {
		Long accountId = getUserAccount().getIdCompte();
		String login =  getUserAccount().getLogin();
		requestService.addEvent(request, accountId, "TRACE", login, "Connexion");

		
		return "prof/userHomePage";

	}
	
	@GetMapping("/cadreadmin/showHome")
	public String showCadreAdminHomePage(HttpServletRequest request) {
		Long accountId = getUserAccount().getIdCompte();
		String login =  getUserAccount().getLogin();
		requestService.addEvent(request, accountId, "TRACE", login, "Connexion");
		return "cadreadmin/userHomePage";

	}

	@GetMapping("/admin/showAdminHome")
	public String showAdminHome(HttpServletRequest request) {
		Long accountId = getUserAccount().getIdCompte();
		String login =  getUserAccount().getLogin();
		requestService.addEvent(request, accountId, "TRACE", login, "Connexion");
		return "admin/adminHome";

	}

}
