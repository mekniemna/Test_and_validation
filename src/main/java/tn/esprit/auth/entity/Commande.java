package tn.esprit.auth.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Commande implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long reference;
	private String date;
	private String status;
	private float total;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Livre> livres ;
	
	
	public Commande(String date, String status, float total) {
		super();
		this.date = date;
		this.status = status;
		this.total = total;
	}
	
	
	public Commande() {
		super();
	}


	public Long getReference() {
		return reference;
	}
	public void setReference(Long reference) {
		this.reference = reference;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public List<Livre> getLivres() {
		return livres;
	}
	public void setLivres(List<Livre> livres) {
		this.livres = livres;
	}
	
	
	

}
