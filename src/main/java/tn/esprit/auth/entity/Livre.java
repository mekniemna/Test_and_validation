package tn.esprit.auth.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import tn.esprit.auth.user.model.User;

@Entity
public class Livre implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reference;
	
	private String titre;
	private String description;
	private String auteur;
	private float prix;
	private boolean disponibilite;
	private int quantite;
	private double note=-1;
	private int nbComment=0;
	
	@JsonIgnore
	@ManyToOne
	private Offre offre;
	
	@JsonIgnore
	@ManyToOne
	private WishList wishList;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "livre")
	@JsonProperty("feedbacks")
	private List<Feedback> feedbacks ;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> users ;
	
	@ManyToMany(cascade = CascadeType.ALL,mappedBy = "livres")
	private List<Commande> commandes ;
	
	public Livre(Long reference, String titre, String description, String auteur, float prix, boolean disponibilite,
			int quantite) {
		this.reference = reference;
		this.titre = titre;
		this.description = description;
		this.auteur = auteur;
		this.prix = prix;
		this.disponibilite = disponibilite;
		this.quantite = quantite;
	}
	

	public Livre() {
		super();
	}

	public Long getReference() {
		return reference;
	}
	public void setReference(Long reference) {
		this.reference = reference;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuteur() {
		return auteur;
	}
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	public boolean isDisponibilite() {
		return disponibilite;
	}
	public void setDisponibilite(boolean disponibilite) {
		this.disponibilite = disponibilite;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}


	public Offre getOffre() {
		return offre;
	}


	public void setOffre(Offre offre) {
		this.offre = offre;
	}


	public WishList getWishList() {
		return wishList;
	}


	public void setWishList(WishList wishList) {
		this.wishList = wishList;
	}


	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}


	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public List<Commande> getCommandes() {
		return commandes;
	}


	public void setCommandes(List<Commande> commandes) {
		this.commandes = commandes;
	}


	public double getNote() {
		return note;
	}


	public void setNote(double note) {
		this.note = note;
	}


	public int getNbComment() {
		return nbComment;
	}


	public void setNbComment(int nbComment) {
		this.nbComment = nbComment;
	}
	
	
}
