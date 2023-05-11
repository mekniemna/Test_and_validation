package tn.esprit.auth.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import tn.esprit.auth.user.model.User;
@Entity
public class WishList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int nombreDeLivre;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "wishList")
	private List<Livre> livres = new ArrayList<>();
	
	@OneToOne(mappedBy = "wishList")
	private User user;
	
	public WishList(int nombreDeLivre) {
		super();
		this.nombreDeLivre = nombreDeLivre;
	}
	
	
	public WishList() {
		super();
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getNombreDeLivre() {
		return nombreDeLivre;
	}
	public void setNombreDeLivre(int nombreDeLivre) {
		this.nombreDeLivre = nombreDeLivre;
	}


	public List<Livre> getLivres() {
		return livres;
	}


	public void setLivres(List<Livre> livres) {
		this.livres = livres;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
}
