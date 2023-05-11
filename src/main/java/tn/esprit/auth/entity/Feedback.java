package tn.esprit.auth.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import tn.esprit.auth.user.model.User;

@Entity
public class Feedback implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(value="f_id")
	private Long id;
	private double note=-1;
	private String commentaire;
	
	private boolean archiver = false;
	
	@JsonIgnore
	@ManyToOne
	private Livre livre;
	
	@JsonIgnore
	@ManyToOne
	private Offre offre;
	
	@ManyToOne(targetEntity = User.class)
	private User user;
	
	public Feedback(Long id) {
		super();
		this.id = id;
	}

	public Feedback(int note, String commentaire) {
		super();
		this.note = note;
		this.commentaire = commentaire;
	}
	
	public Feedback() {
		super();
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getNote() {
		return note;
	}
	public void setNote(double note) {
		this.note = note;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Livre getLivre() {
		return livre;
	}

	public void setLivre(Livre livre) {
		this.livre = livre;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Offre getOffre() {
		return offre;
	}

	public void setOffre(Offre offre) {
		this.offre = offre;
	}

	public boolean isArchiver() {
		return archiver;
	}

	public void setArchiver(boolean archiver) {
		this.archiver = archiver;
	}
	
	

}
