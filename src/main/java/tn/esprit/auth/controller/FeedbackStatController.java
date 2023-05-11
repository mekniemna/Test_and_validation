package tn.esprit.auth.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.auth.entity.FeedBackStat;
import tn.esprit.auth.entity.Livre;
import tn.esprit.auth.entity.Offre;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.service.FeedbackStatService;


@RestController
@RequestMapping("/statistique")
public class FeedbackStatController {
	
	@Autowired
	private FeedbackStatService feedbackStatService;
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping()
	public Response<FeedBackStat> getAllStat(@RequestBody Map<String,String> dates)
	{
		return feedbackStatService.getAllFeedback(dates);
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/book")
	public Response<FeedBackStat> getBookStat(@RequestBody Map<String,String> dates)
	{
		return feedbackStatService.getBookStat(dates);
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/offer")
	public Response<FeedBackStat> getOfferStat(@RequestBody Map<String,String> dates)
	{
		return feedbackStatService.getOfferStat(dates);
	}
	
//	----For Ennaceur + yassine
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/best-rated-book")
	public Livre getBestRatedBook() {
		return feedbackStatService.getBestRatedBook();
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/best-rated-offer")
	public Offre getBestRatedOffer() {
		return feedbackStatService.getBestRatedOffer();
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/popular-book")
	public Livre getPopularBook() {
		return feedbackStatService.getPopularBook();
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/popular-offer")
	public Offre getPopularOffer() {
		return feedbackStatService.getPopularOffer();
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/list-popular-book")
	public List<Livre> getListPopularBook() {
		return feedbackStatService.geListPopularBook();
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/list-popular-offer")
	public List<Offre> getListPopularOffer() {
		return feedbackStatService.geListPopularOffer();
	}
	
}
