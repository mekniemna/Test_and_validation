package tn.esprit.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.auth.entity.Feedback;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.service.FeedbackService;

@RequestMapping("/feedback")
@RestController
public class FeedbackController {
	@Autowired
	private FeedbackService service;
	
//	------------------------------BOOK
	@GetMapping("/livre/{id}")
	public List<Feedback> findAllFeebackOfABook(@PathVariable Long id)
	{
		return service.findAllFeebackOfABook(id);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/livre/{id}")
	public Response<Boolean> addFeebacks(@PathVariable Long id , @RequestBody Feedback feedback,@CurrentSecurityContext(expression = "authentication?.name") String username)
	{
		return service.addFeebacks(id, username , feedback);
	}
	

	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/livre/{id}")
	public Response<Boolean> updateUserComment(@PathVariable Long id , @RequestBody Feedback feedback,@CurrentSecurityContext(expression = "authentication?.name") String username)
	{
		return service.updateUserFeedback(id, username , feedback);
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT')")
	@DeleteMapping("/all/livre/{id}")
	public Response<Boolean> deleteAllFeedBackOfOneBook(@PathVariable Long id) {
		return	service.deleteAllFeedBackOfOneBook(id);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/livre/{id}")
	public Response<Boolean> deleteUserComment(@PathVariable Long id,@CurrentSecurityContext(expression = "authentication?.name") String username,@RequestBody(required=true) Feedback feedback) {
		return	service.deleteUserComment(id,username,feedback);
	}
	
//	------------------------------OFFER
	@GetMapping("/offre/{id}")
	public List<Feedback> findAllFeebackOfAOffer(@PathVariable Long id)
	{
		return service.findAllFeebackOfAOffer(id);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/offre/{id}")
	public Response<Boolean> addFeebacksInOffer(@PathVariable Long id , @RequestBody Feedback feedback,@CurrentSecurityContext(expression = "authentication?.name") String username)
	{
		return service.addFeebacksInOffer(id, username , feedback);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/offre/{id}")
	public Response<Boolean> updateUserOfferComment(@PathVariable Long id , @RequestBody Feedback feedback,@CurrentSecurityContext(expression = "authentication?.name") String username)
	{
		return service.updateUserFeedbackOfAnOffer(id, username , feedback);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/offre/{id}")
	public Response<Boolean> deleteUserOfferComment(@PathVariable Long id,@CurrentSecurityContext(expression = "authentication?.name") String username,@RequestBody(required=true) Feedback feedback) {
		return	service.deleteUserOfferComment(id,username,feedback);
	}
}
