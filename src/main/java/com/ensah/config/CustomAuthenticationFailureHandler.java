package com.ensah.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ensah.core.bo.Compte;
import com.ensah.core.bo.UserPrincipal;
import com.ensah.core.bo.Utilisateur;
import com.ensah.core.dao.ICompteDao;
import com.ensah.core.services.ICompteService;
import com.ensah.core.services.impl.CompteServiceImpl;
import com.ensah.core.services.impl.CustomAuthentificationService;
import com.ensah.core.web.controllers.LoginController;
import com.ensah.core.web.models.UserAndAccountInfos;
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
  
	@Autowired
	private ICompteService userService;
		
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		  // find the corresponding account 
		 String login = request.getParameter("username");
	     Compte user = userService.findByUsername(login);
	     //System.out.print("captcha"+request.getParameter("g-recaptcha-response"));
         //
	     if (user != null) {
	            if (user.isEnabled() && user.isAccountNonLocked()) {
	                if (user.getFailedAttempt() < CompteServiceImpl.MAX_FAILED_ATTEMPTS - 1) {
	                    userService.increaseFailedAttempts(user);
	                } else {
	                    userService.lock(user);
	                    exception = new LockedException("Your account has been locked due to 3 failed attempts."
	                            + " It will be unlocked after 24 hours.");
	                }
	            } else if (!user.isAccountNonLocked()) {
	                if (userService.unlockWhenTimeExpired(user)) {
	                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
	                }
	            }
	             
	        }
		

		if (exception instanceof DisabledException) {
			response.sendRedirect("showMyLoginPage?error=disabled");
			return;

		} 
		
		else if (exception instanceof LockedException) {
			response.sendRedirect("showMyLoginPage?error=locked");
			return;
		} 
		else if (exception instanceof CredentialsExpiredException) {
			response.sendRedirect("showMyLoginPage?error=expired");
			return;

		} 
		else {
			//httpSession.setAttribute(accountLogin, );
			response.sendRedirect("showMyLoginPage?error=other");
			
			
		}
	}
	
	

}