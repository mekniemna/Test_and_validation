package tn.esprit.auth.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.auth.entity.FeedBackStat;
import tn.esprit.auth.entity.Livre;
import tn.esprit.auth.entity.Offre;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.model.ResponseService;
import tn.esprit.auth.repository.FeedbackStatRepo;
import tn.esprit.auth.repository.LivreRepository;
import tn.esprit.auth.repository.OffreRepository;

@Service
public class FeedbackStatService {
	
	@Autowired
	private FeedbackStatRepo repo;
	@Autowired
	private LivreRepository livreRepo;
	@Autowired
	private OffreRepository offreRepository;
	
	public Response<FeedBackStat> getAllFeedback(Map<String, String> dates){
		LocalDate start = getFirstDateRow();
		LocalDate end = LocalDate.now();
		FeedBackStat feedBackStat = null , finalFeedBackStat = null;
		if(dates.get("startDate") != null)
			start = LocalDate.parse(dates.get("startDate"));
		
		if(dates.get("endDate") != null)
			end = LocalDate.parse(dates.get("endDate"));
		List<FeedBackStat> feedBackStats_1 = repo.findAllByDate(start);
		List<FeedBackStat> feedBackStats_2 = repo.findAllByDate(end);
		
		feedBackStat = calculateStatic(feedBackStat,feedBackStats_1);
		finalFeedBackStat = calculateStatic(feedBackStat, feedBackStats_2);
		
		return new ResponseService<FeedBackStat>().getSuccessResponse(finalFeedBackStat);
		
	}
	
	private FeedBackStat calculateStatic(FeedBackStat feedBackStat,List<FeedBackStat> feedBackStats_1)
	{
		if(feedBackStat == null)
			feedBackStat = new FeedBackStat();
		
		for (FeedBackStat feed : feedBackStats_1) {
			feedBackStat.setNbPositiveCommentsBook(feedBackStat.getNbPositiveCommentsBook()+feed.getNbPositiveCommentsBook());
			feedBackStat.setNbNegativeCommentsBook(feedBackStat.getNbNegativeCommentsBook()+feed.getNbNegativeCommentsBook());
			feedBackStat.setNbRejectedCommentsBook(feedBackStat.getNbRejectedCommentsBook()+feed.getNbRejectedCommentsBook());
			
			feedBackStat.setNbPositiveCommentsOffer(feedBackStat.getNbPositiveCommentsOffer()+feed.getNbPositiveCommentsOffer());
			feedBackStat.setNbNegativeCommentsOffer(feedBackStat.getNbNegativeCommentsOffer()+feed.getNbNegativeCommentsOffer());
			feedBackStat.setNbRejectedCommentsOffer(feedBackStat.getNbRejectedCommentsOffer()+feed.getNbRejectedCommentsOffer());
		}
		return feedBackStat;
	}

	public Response<FeedBackStat> getBookStat(Map<String, String> dates) {
		LocalDate start = getFirstDateRow();
		LocalDate end = LocalDate.now();
		FeedBackStat feedBackStat = null , finalFeedBackStat = null;
		if(dates.get("startDate") != null)
			start = LocalDate.parse(dates.get("startDate"));
		
		if(dates.get("endDate") != null)
			end = LocalDate.parse(dates.get("endDate"));
		List<FeedBackStat> feedBackStats_1 = repo.findAllByDate(start);
		List<FeedBackStat> feedBackStats_2 = repo.findAllByDate(end);
		
		feedBackStat = calculateBookStatic(feedBackStat,feedBackStats_1,true);
		finalFeedBackStat = calculateBookStatic(feedBackStat, feedBackStats_2,true);
		return new ResponseService<FeedBackStat>().getSuccessResponse(finalFeedBackStat);
	}
	
	public Response<FeedBackStat> getOfferStat(Map<String, String> dates) {
		LocalDate start = getFirstDateRow();
		LocalDate end = LocalDate.now();
		FeedBackStat feedBackStat = null , finalFeedBackStat = null;
		if(dates.get("startDate") != null)
			start = LocalDate.parse(dates.get("startDate"));
		
		if(dates.get("endDate") != null)
			end = LocalDate.parse(dates.get("endDate"));
		List<FeedBackStat> feedBackStats_1 = repo.findAllByDate(start);
		List<FeedBackStat> feedBackStats_2 = repo.findAllByDate(end);
		
		feedBackStat = calculateBookStatic(feedBackStat,feedBackStats_1,false);
		finalFeedBackStat = calculateBookStatic(feedBackStat, feedBackStats_2,false);
		return new ResponseService<FeedBackStat>().getSuccessResponse(finalFeedBackStat);
	}
	
	private LocalDate getFirstDateRow() {
		LocalDate start = LocalDate.now() ;
		for(FeedBackStat feedBackStat : repo.findAll())
		{
			if(feedBackStat.getDate().isBefore(start))
				start = feedBackStat.getDate();
		}
		return start;
	}
	
	
	private FeedBackStat calculateBookStatic(FeedBackStat feedBackStat,List<FeedBackStat> feedBackStats_1,boolean isBook)
	{
		if(feedBackStat == null)
			feedBackStat = new FeedBackStat();
		FeedBackStat finalObject ;
		for (FeedBackStat feed : feedBackStats_1) {
			if(isBook)
			{
				feedBackStat.setNbPositiveCommentsBook(feedBackStat.getNbPositiveCommentsBook()+feed.getNbPositiveCommentsBook());
				feedBackStat.setNbNegativeCommentsBook(feedBackStat.getNbNegativeCommentsBook()+feed.getNbNegativeCommentsBook());
				feedBackStat.setNbRejectedCommentsBook(feedBackStat.getNbRejectedCommentsBook()+feed.getNbRejectedCommentsBook());
			}
			else
			{
				feedBackStat.setNbPositiveCommentsOffer(feedBackStat.getNbPositiveCommentsOffer()+feed.getNbPositiveCommentsOffer());
				feedBackStat.setNbNegativeCommentsOffer(feedBackStat.getNbNegativeCommentsOffer()+feed.getNbNegativeCommentsOffer());
				feedBackStat.setNbRejectedCommentsOffer(feedBackStat.getNbRejectedCommentsOffer()+feed.getNbRejectedCommentsOffer());
			}
		}
		
		if(isBook)
			finalObject = new FeedBackStat( -1, feedBackStat.getNbPositiveCommentsBook(), feedBackStat.getNbNegativeCommentsBook(), feedBackStat.getNbRejectedCommentsBook(),-1,-1,-1, LocalDate.now());
		else
			finalObject = new FeedBackStat(-1, -1,-1,-1,feedBackStat.getNbPositiveCommentsOffer(), feedBackStat.getNbNegativeCommentsOffer(), feedBackStat.getNbRejectedCommentsOffer(), LocalDate.now());

		return finalObject;
	}

//	----For Ennaceur + yassine
	public Livre getBestRatedBook() {
		Livre bestRatedBook = new Livre();
		for(Livre book : livreRepo.findAll())
		{
			if(book.getNote() > bestRatedBook.getNote())
				bestRatedBook= book;
		}
		return bestRatedBook;
	}

	public Offre getBestRatedOffer() {
		Offre bestRatedOffre = new Offre();
		for(Offre offre : offreRepository.findAll())
		{
			if(offre.getNote() > bestRatedOffre.getNote())
				bestRatedOffre= offre;
		}
		return bestRatedOffre;
	}

	public Livre getPopularBook() {
		Livre bestRatedBook = new Livre();
		for(Livre book : livreRepo.findAll())
		{
			if(book.getNote()+book.getNbComment() > bestRatedBook.getNote()+bestRatedBook.getNbComment())
				bestRatedBook= book;
		}
		return bestRatedBook;
	}

	public Offre getPopularOffer() {
		Offre bestRatedOffre = new Offre();
		for(Offre offre : offreRepository.findAll())
		{
			if(offre.getNote()+offre.getNbComment() > bestRatedOffre.getNote()+bestRatedOffre.getNbComment())
				bestRatedOffre= offre;
		}
		return bestRatedOffre;
	}

	public List<Livre> geListPopularBook() {
		List<Livre> list = livreRepo.findAllByDisponibilite(true);
		Collections.sort(list, new Comparator<Livre>() {
            @Override
            public int compare(Livre o1, Livre o2) {
            	int count_1 = (int)o1.getNote()+o1.getNbComment();
            	int count_2 = (int)o2.getNote()+o2.getNbComment();
                return count_1 - count_2 ;
            }
        });
		Collections.reverse(list);
		List<Livre> popularBook =  new ArrayList<>();
		int len = list.size() >= 5 ? 5 : list.size() ;
		for(int i = 0 ;i<len;i++) {
			popularBook.add(list.get(i));
		}
		return popularBook;
	}

	public List<Offre> geListPopularOffer() {
		List<Offre> list = offreRepository.findAllByDiponibilite(true);
		Collections.sort(list, new Comparator<Offre>() {
            @Override
            public int compare(Offre o1, Offre o2) {
            	int count_1 = (int)o1.getNote()+o1.getNbComment();
            	int count_2 = (int)o2.getNote()+o2.getNbComment();
                return count_1 - count_2 ;
            }
        });
		Collections.reverse(list);
		List<Offre> popularOffer =  new ArrayList<>();
		int len = list.size() >= 5 ? 5 : list.size() ;
		for(int i = 0 ;i<len;i++) {
			popularOffer.add(list.get(i));
		}
		return popularOffer;
	}


}
