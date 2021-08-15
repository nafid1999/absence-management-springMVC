package com.ensah.core.web.controllers;

import java.io.IOException;
import  com.ensah.core.services.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ensah.core.services.IPersonService;
import com.ensah.core.utils.ExcelExporter;
import com.ensah.core.bo.CadreAdministrateur;
import com.ensah.core.bo.Compte;
import com.ensah.core.bo.Enseignant;
import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.JournalisationEvenements;
import com.ensah.core.bo.Role;
import com.ensah.core.bo.Utilisateur;
import com.ensah.core.services.ICompteService;
import com.ensah.core.web.models.AccountModel;
import com.ensah.core.web.models.PersonModel;



@Controller
@RequestMapping("/admin") // Très important car, dans Spring security les URL qui commencent par ADMIN
							// sont dédiées
							// à l'admin. Toutes les URL dédinies dans ce controleur définissent des
							// fonctionnalités
							// accessibles uniquement à l'administrateur
public class AccountController {

	@Autowired
	private ICompteService userService; // On obtient par injection automatique le service

	@Autowired
	private IPersonService personService; // On obtient par injection automatique le service
	
	@Autowired
	private IRequestService requestService;
	
	//Cette méthode initialise le formulaire de création de compte
	@RequestMapping(value = "createAccountForm/{idPerson}", method = RequestMethod.GET)
	public String createAccountForm(@PathVariable  int idPerson, Model model) {

		//On crée le model 
		AccountModel accountModel = new AccountModel(Long.valueOf(idPerson));

		//On enregistre le modèle pour le passer à la vue
		model.addAttribute("accountModel", accountModel);

		//On ajoute la liste des roles dans le modele 
		model.addAttribute("roleList", userService.getAllRoles());
		
		//On ajoute également la liste des comptes dans le modèle
		model.addAttribute("accountList", userService.getAllAccounts());

		
		//On affiche la vue
		return "admin/formAccount";
	}

	@GetMapping("manageAccounts")
	public String manageAccounts(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {
         
		model.addAttribute("accountList", userService.getAllAccounts());
		return "admin/accountList";
	}
	@GetMapping("createAccounts")
	public String createAccounts(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {


		model.addAttribute("personList", personService.getAllPersons());

		return "admin/accountCreation";
	}
	
	
	//Cette méthode permet de créer un comote
	@PostMapping("addAccount")
	public String addAccount(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {

		
		//La création du compte est implémenter au niveau service
		//Il suffit de passer l'id du role et l'id de personne
		//à la couche service
		String password = userService.createUser(accountModel.getRoleId(), accountModel.getPersonId());

		//On affiche le mot de passe dans la vue 
		accountModel.setPassword(password);
		
		//On affiche également la liste des comptes dans la vue
		model.addAttribute("accountList", userService.getAllAccounts());

		
		//On affiche la vue 
		return "/admin/accountList";

	}
	
	/*******************
	 * 
	 * activate account 
	 * 
	 * *****************/
	@GetMapping(value="/activate/{idAccount}")
	public String activate(@PathVariable int idAccount, Model model) {
		
		Compte account= userService.getAccountById(Long.valueOf(idAccount));
		account.setEnabled(true);
		userService.update(account);
		
		return "redirect:/admin/manageAccounts";
	}
	/***********************
	 * 
	 * desactivate account 
	 * 
	 * ********************/
	@RequestMapping(value="/desactivate/{idAccount}",method = RequestMethod.GET)
	public String desactivate(@PathVariable int idAccount, Model model) {
     
		Compte account= userService.getAccountById(Long.valueOf(idAccount));
		account.setEnabled(false);
		userService.update(account);
		
		return "redirect:/admin/manageAccounts";

	}
	
	/*******************
	 * 
	 * reset password
	 * 
	 * *****************/
	
	@RequestMapping(value="/resetPasswod/{idAccount}",method = RequestMethod.GET)
	public String resetPassword(@PathVariable int idAccount, Model model) {
     
		Compte account= userService.getAccountById(Long.valueOf(idAccount));
		String password=userService.renitialisePassword(account.getIdCompte());
		model.addAttribute("reset_password",password);
		model.addAttribute("accountList", userService.getAllAccounts());
		
		return "/admin/accountList";

	}
	
	
	
	/****************************************
	 * 
	 * display actions for a spesefic account
	 * 
	 * ***************************************/
	@RequestMapping(value ="displayEventsByAccount/{idAccount}", method = RequestMethod.GET)
	public String afficheActionsByAccount(@PathVariable Long idAccount, Model model) {

		Compte account =userService.getAccountById(Long.valueOf(idAccount));
		model.addAttribute("username", account.getLogin());
		//all events
		ArrayList<JournalisationEvenements> ALL_events=(ArrayList<JournalisationEvenements>) requestService.getActionEventsByAccount(idAccount);
		
		//add list of events to the model.
		
		
		
		
		
		
		model.addAttribute("eventlist", ALL_events);
				model.addAttribute("connexion_event","Connexion" );

		return "admin/listActions_by_account";

	}
	
	/*****************************
	 * 
	 * display the change role page
	 * 
	 * ****************************/
	@RequestMapping(value="/changeRole/{idAccount}",method = RequestMethod.GET)
	public String changeRole(@PathVariable int idAccount, Model model) {
     
				//create account
				Compte account= userService.getAccountById(Long.valueOf(idAccount));
				
				model.addAttribute("account", account);
				
				//On crée le model 
				AccountModel accountModel = new AccountModel(Long.valueOf(idAccount),account.getLogin());

				//On enregistre le modèle pour le passer à la vue
				model.addAttribute("accountModel", accountModel);
				
				//retrieve all roles
				ArrayList<Role> all_roles=(ArrayList<Role>) userService.getAllRoles();
				

				//On ajoute la liste des roles dans le modele 
				model.addAttribute("roleList", all_roles);
		
		
		return "/admin/changeRole";

	}
	/****************************************
	 * 
	 * update role of account
	 * 
	 * ***************************************/
	@PostMapping("updateRole")
	public String modifyRole(@ModelAttribute("accountModel") AccountModel accountModel, Model model) {

		 userService.updateRoleUser(accountModel.getRoleId(), accountModel.getAccountId());

		//On affiche également la liste des comptes dans la vue
		model.addAttribute("accountList", userService.getAllAccounts());
		
		//On affiche la vue 
		return "/admin/accountList";

	}
	
	
	
	/**************************
	 * 
	 * display all histories
	 * 
	 * **************************/
	@RequestMapping("/historique")
	public String  createHistorique(Model model) {
		model.addAttribute("eventlist", requestService.getConnectionEvents("Connexion"));
		System.out.print(requestService.getAllEvents());
		return "admin/historic";
	}
	
	/**************************
	 * 
	 * display actions
	 * 
	 * **************************/
	@RequestMapping("/actions")
	public String  saveAction(Model model) {
		model.addAttribute("eventlist", requestService.getConnectionEvents("ACTION"));
		return "admin/actionList";
	}
	
	/**************************
	 * 
	 * search by CIN O CNE
	 * 
	 * **************************/
	@RequestMapping(value = "serachPersonByCniOrCne", method = RequestMethod.GET)
	public String serachPerson(@RequestParam String cinOrcne, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl2 = personService.getEtudiantByCne(cinOrcne);
		Utilisateur utl = personService.getPersonByCin(cinOrcne);

		if (utl == null && utl2==null) {

			// Initialiser le modele avec la personne
			model.addAttribute("personModel", new ArrayList<PersonModel>());
		} else if(utl!=null) {
		
			// On construit le modèle
			PersonModel pm = new PersonModel();

			// En fonction due type de l'utilisateur à modifier
			// Ceci va nous pemettre d'afficher un formulaire adapté
			// slon le type de la personne
			if (utl instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
			} else if (utl instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
			} else if (utl instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) utl, pm);				
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);

			}
			List<PersonModel> modelPersons = new ArrayList<PersonModel>();
			modelPersons.add(pm);
			// Initialiser le modele avec la personne
			model.addAttribute("personList", modelPersons);
		}else {
			// On construit le modèle
				PersonModel pm = new PersonModel();
				BeanUtils.copyProperties((Etudiant) utl2, pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
				
				List<PersonModel> modelPersons = new ArrayList<PersonModel>();
				modelPersons.add(pm);
				// Initialiser le modele avec la personne
				model.addAttribute("personList", modelPersons);
			
		}
		return "admin/accountCreation";
	}
	
	/**************************
	 * 
	 * search by login
	 * 
	 * **************************/
	
	@RequestMapping("/searchByLogin")
	public String  serchByLogin(@RequestParam String login,Model model) {

		List<Compte> Accounts= new ArrayList<Compte>();
		if(userService.findByUsername(login)!=null) {
			Accounts.add(userService.findByUsername(login));
			model.addAttribute("accountList",Accounts );
			
			return "admin/accountList";
		}
		
		model.addAttribute("accountList",null );

	
		return "admin/accountList";

	}
	
	@GetMapping("exportAccounts")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=accounts_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Compte> comptes = userService.getAllAccounts();

		ExcelExporter excelExporter = userService.prepareCompteExport(comptes);

		excelExporter.export(response);
	}
	

}
