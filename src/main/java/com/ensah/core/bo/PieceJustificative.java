package com.ensah.core.bo;


import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class PieceJustificative {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPieceJustificative;

	private String cheminFichier;

	private String intitule;

	private Date dateLivraison;

	private int etat;

	private String source;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable( joinColumns = @JoinColumn(name="idPieceJustificative"),
				inverseJoinColumns = @JoinColumn(name="idAbsence"))
	private List<Absence> absence;

	public Long getIdPieceJustificative() {
		return idPieceJustificative;
	}

	public void setIdPieceJustificative(Long idPieceJustificative) {
		this.idPieceJustificative = idPieceJustificative;
	}

	public String getCheminFichier() {
		return cheminFichier;
	}

	public void setCheminFichier(String cheminFichier) {
		this.cheminFichier = cheminFichier;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Date getDateLivraison() {
		return dateLivraison;
	}

	public void setDateLivraison(Date dateLivraison) {
		this.dateLivraison = dateLivraison;
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Absence> getAbsence() {
		return absence;
	}

	public void setAbsence(List<Absence> absence) {
		this.absence = absence;
	}

	
	
	
}