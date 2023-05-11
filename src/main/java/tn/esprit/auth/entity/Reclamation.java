package tn.esprit.auth.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Reclamation {
	
	
	@Id
	private int reference;
	private String description;
	private String type;
	
	
	public int getReference() {
		return reference;
	}
	public void setReference(int reference) {
		this.reference = reference;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
	

}
