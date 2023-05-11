package tn.esprit.auth.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.auth.entity.Livre;
import tn.esprit.auth.entity.Offre;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.model.ResponseService;
import tn.esprit.auth.repository.LivreRepository;
import tn.esprit.auth.repository.OffreRepository;

@Service
public class OffreService {
	private static Logger logger = LogManager.getLogger(OffreService.class);

	@Autowired
	private OffreRepository OffreRepo;

	@Autowired
	private LivreRepository livreRepo;

//	CRUD : 
//	- add ( offre / offre + [livres] ) 
//	- update [ livre(ajout/supp) + info ] **
//	- delete [deleteById* / deleteAll* / deleteAllBooks* /deleteBook] **
//	- READ [all* / byId* / allBook* / bookById*] **

//	------------------READ

	public List<Livre> findAllBooks(Long offreId) {
		if(existsById(offreId))
			return OffreRepo.findById(offreId).get().getLivres();
		else
			return null;
			
	}

	public Response<Livre> findBookById(Long offreId) {
		setOffreDiponibility();
		if (livreRepo.existsByOffreReference(offreId)) {
			return new ResponseService<Livre>().getSuccessResponse(livreRepo.findByOffreReference(offreId));
		} else
			return new ResponseService<Livre>().getFailedResponse("The book does not exist in this offer ...");
	}

	public List<Offre> findAll() {
		setOffreDiponibility();
		List<Offre> offres = new ArrayList<>();
		OffreRepo.findAll().forEach(offre -> {
			if (offre.isDiponibilite()) {
				offres.add(offre);
			}
		});
		return offres;
	}

	public Response<Offre> findById(Long id) {
		setOffreDiponibility();
		if (existsById(id)) {
			return new ResponseService<Offre>().getSuccessResponse(OffreRepo.findById(id).get());
		} else
			return new ResponseService<Offre>().getFailedResponse("This offer does not exist...");
	}

	public boolean existsById(Long id) {
		setOffreDiponibility();
		return (OffreRepo.existsById(id) && OffreRepo.findById(id).get().isDiponibilite());
	}

//	------------------ADD
	public <S extends Offre> S save(S offre) {
		return OffreRepo.save(offre);
	}

	public Response<Offre> saveBook(Long livreId, Long offreId) {
		if (existsById(offreId)) {
			Offre oldOffre = OffreRepo.findById(offreId).get();
			List<Livre> oldOBooks = livreRepo.findAllByOffreReference(offreId);
			logger.info("--oldOffre.getQuantite() = " + (oldOffre.getQuantite()) + "/ oldOBooks.size() "
					+ (oldOBooks.size()));
			if (oldOffre.getQuantite() > oldOBooks.size()) {
				if (livreRepo.existsById(livreId)) {
					if (!BookExistByOfferId(oldOffre, livreId)) {
						Livre livre = livreRepo.findById(livreId).get();

						oldOBooks.add(livre);
						oldOffre.setLivres(oldOBooks);
						double price = oldOffre.getPrix_total()+livre.getPrix();
						double pricePer = price-((price*oldOffre.getPourcentage())/100);
						logger.info("------price total "+price+"/....pricePer = "+pricePer);
						oldOffre.setPrix_total(price);
						oldOffre.setPrix_pourcentage(pricePer);
						livre.setOffre(oldOffre);
						livreRepo.save(livre);
						return new ResponseService<Offre>().getSuccessResponse(oldOffre);
					} else
						return new ResponseService<Offre>().getFailedResponse("this book exist in this offer...");

				} else
					return new ResponseService<Offre>().getFailedResponse("this book does not exist...");
			} else
				return new ResponseService<Offre>().getFailedResponse("list is full");

		}
		return new ResponseService<Offre>().getFailedResponse("this offer does not exist...");

	}

//	------------------UPDATE 
	public Response<Offre> update(Offre offre, Long Oid) {
		if (existsById(Oid)) {
			offre.setReference(Oid);
			return new ResponseService<Offre>().getSuccessResponse(OffreRepo.save(offre));
		} else
			return new ResponseService<Offre>().getFailedResponse("this offer does not exist...");
	}

//	------------------DELETE
	public Response<Boolean> deleteById(Long id) {
		if (existsById(id)) {
			List<Livre> livres = OffreRepo.findById(id).get().getLivres();
			livres.stream().forEach(l->{
				l.setOffre(null);
				livreRepo.save(l);
			});
			OffreRepo.deleteById(id);
			return new ResponseService<Boolean>().getSuccessResponse(true);
		} else
			return new ResponseService<Boolean>().getFailedResponse("this offer does not exist...");
	}

	public Response<Boolean> deleteAll() {
		OffreRepo.findAll().stream().forEach(offre->{
			offre.getLivres().stream().forEach(livre->{
				livre.setOffre(null);
				livreRepo.save(livre);
			});
		});
		OffreRepo.deleteAll();
		return new ResponseService<Boolean>().getSuccessResponse(true);
	}

	public Response<Boolean> deleteBooks(Long offreId) {
		Offre oldOffre = OffreRepo.findById(offreId).get();
		List<Livre> oldOBooks = oldOffre.getLivres();
		if (oldOBooks.size() > 0) {
			oldOBooks.stream().forEach(c -> {
				c.setOffre(null);
				livreRepo.save(c);
			});
			oldOBooks.removeAll(oldOBooks);
			oldOffre.setLivres(oldOBooks);

			logger.info("**list offer size =" + (oldOffre.getLivres().size()));
//			add
			oldOffre.setPrix_total(0);
			oldOffre.setPrix_pourcentage(0);
			OffreRepo.save(oldOffre);
			return new ResponseService<Boolean>().getSuccessResponse(true);
		} else
			return new ResponseService<Boolean>().getFailedResponse("this offer doesn't have books...");
	}

	public Response<Boolean> deleteBookById(Long offreId, Long bookId) {
		if (existsById(offreId)) {
			Offre oldOffre = OffreRepo.findById(offreId).get();
			if (BookExistByOfferId(oldOffre, bookId)) {
				logger.info("---olivre .. " + (livreRepo.existsByOffreReference(offreId)));
				Livre livre = livreRepo.findById(bookId).get();
				List<Livre> oldOBooks = oldOffre.getLivres();
				logger.info("---index of the book to delete .. " + oldOBooks.indexOf(livre));
				oldOBooks.remove(oldOBooks.indexOf(livre));
				
//				add
				oldOffre.setPrix_total(oldOffre.getPrix_total()-livre.getPrix());
				oldOffre.setPrix_pourcentage((oldOffre.getPrix_total())-((oldOffre.getPrix_total()*oldOffre.getPourcentage())/100));
				
				livre.setOffre(null);
				livreRepo.save(livre);
//				add
				OffreRepo.save(oldOffre);
				return new ResponseService<Boolean>().getSuccessResponse(true);
			} else
				return new ResponseService<Boolean>().getFailedResponse("this book does not exist...");
		} else
			return new ResponseService<Boolean>().getFailedResponse("this offer does not exist...");

	}

	public Boolean BookExistByOfferId(Offre offre, Long bookId) {
		boolean exist = false;
		List<Livre> livres = offre.getLivres();
		for (Livre livre : livres) {
			if (livre.getReference() == bookId) {
				exist = true;
				break;
			}
		}
		return exist;
	}

	public void setOffreDiponibility() {
		DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd",Locale.US);
		LocalDate dateNow = LocalDate.now();
		logger.info("date now ="+dateNow);
		List<Offre> offres = OffreRepo.findAll();
		offres.stream().forEach(offre -> {
			LocalDate offreFinalDate = LocalDate.parse(offre.getDate_fin(),sdf);
			if (offreFinalDate.isAfter(dateNow)) {
				logger.info("date now ="+dateNow+" / fin date = "+offreFinalDate);
				offre.setDiponibilite(true);
			}
			else
				offre.setDiponibilite(false);
		});

	}
}
