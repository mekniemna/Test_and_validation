package tn.esprit.auth.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import tn.esprit.auth.model.MaskingFilter;


@Entity
public class FeedBackStat implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = MaskingFilter.class)
	private int nbPositiveCommentsBook ;
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = MaskingFilter.class)
	private int nbNegativeCommentsBook ;
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = MaskingFilter.class)
	private int nbRejectedCommentsBook ;
	
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = MaskingFilter.class)
	private int nbPositiveCommentsOffer;	
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = MaskingFilter.class)
	private int nbNegativeCommentsOffer ;
	@JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = MaskingFilter.class)
	private int nbRejectedCommentsOffer ;

	private LocalDate date = LocalDate.now();

	public FeedBackStat() {
	}



	public FeedBackStat(int id,int nbPositiveCommentsBook, int nbNegativeCommentsBook, int nbRejectedCommentsBook,
			int nbPositiveCommentsOffer, int nbNegativeCommentsOffer, int nbRejectedCommentsOffer, LocalDate date) {
		super();
		this.id=(long) id;
		this.nbPositiveCommentsBook = nbPositiveCommentsBook;
		this.nbNegativeCommentsBook = nbNegativeCommentsBook;
		this.nbRejectedCommentsBook = nbRejectedCommentsBook;
		this.nbPositiveCommentsOffer = nbPositiveCommentsOffer;
		this.nbNegativeCommentsOffer = nbNegativeCommentsOffer;
		this.nbRejectedCommentsOffer = nbRejectedCommentsOffer;
		this.date = date;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getNbPositiveCommentsBook() {
		return nbPositiveCommentsBook;
	}

	public void setNbPositiveCommentsBook(int nbPositiveCommentsBook) {
		this.nbPositiveCommentsBook = nbPositiveCommentsBook;
	}

	public int getNbNegativeCommentsBook() {
		return nbNegativeCommentsBook;
	}

	public void setNbNegativeCommentsBook(int nbNegativeCommentsBook) {
		this.nbNegativeCommentsBook = nbNegativeCommentsBook;
	}

	public int getNbRejectedCommentsBook() {
		return nbRejectedCommentsBook;
	}

	public void setNbRejectedCommentsBook(int nbRejectedCommentsBook) {
		this.nbRejectedCommentsBook = nbRejectedCommentsBook;
	}

	public int getNbPositiveCommentsOffer() {
		return nbPositiveCommentsOffer;
	}

	public void setNbPositiveCommentsOffer(int nbPositiveCommentsOffer) {
		this.nbPositiveCommentsOffer = nbPositiveCommentsOffer;
	}

	public int getNbNegativeCommentsOffer() {
		return nbNegativeCommentsOffer;
	}

	public void setNbNegativeCommentsOffer(int nbNegativeCommentsOffer) {
		this.nbNegativeCommentsOffer = nbNegativeCommentsOffer;
	}

	public int getNbRejectedCommentsOffer() {
		return nbRejectedCommentsOffer;
	}

	public void setNbRejectedCommentsOffer(int nbRejectedCommentsOffer) {
		this.nbRejectedCommentsOffer = nbRejectedCommentsOffer;
	}

}
