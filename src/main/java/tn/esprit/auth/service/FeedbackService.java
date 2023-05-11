package tn.esprit.auth.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.auth.entity.FeedBackStat;
import tn.esprit.auth.entity.Feedback;
import tn.esprit.auth.entity.Livre;
import tn.esprit.auth.entity.Offre;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.model.ResponseService;
import tn.esprit.auth.repository.FeedbackRepository;
import tn.esprit.auth.repository.FeedbackStatRepo;
import tn.esprit.auth.repository.LivreRepository;
import tn.esprit.auth.repository.OffreRepository;
import tn.esprit.auth.user.model.User;
import tn.esprit.auth.user.repository.UserRepository;

@Service
public class FeedbackService {

	@Autowired
	private LivreRepository livreRepo;

	@Autowired
	private FeedbackRepository feedbackRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OffreRepository offreRepo;

	@Autowired
	private FeedbackStatRepo feedbackStatRepo;

	ArrayList<String> badWord = new ArrayList<>(Arrays.asList("asshole", "playboy", "spoiler", "spoiled", "dreadful",
			"frightful", "crap", "holy crap", "douchewaffle", "dumass", "dumb ass", "assjacker", "a_s_s", "a**hole"));

	ArrayList<String> positiveWord = new ArrayList<>(Arrays.asList("calm", "cheerful", "cool", "happy", "mild", "nice",
			"peaceful", "pleased", "content", "joyful", "joyous"));

	ArrayList<String> negativeWord = new ArrayList<>(Arrays.asList("awful", "terrible", "disrespectful", "useless",
			"appalling", "mess", "cruel", "horrible", "disgusting", "dishonorable", "disheveled", "offensive"));

//	---------------------------------------------------BOOK
//	--------------READ
	public List<Feedback> findAllFeebackOfABook(Long id) {
		List<Feedback> feedbacks = new ArrayList<>();
		if (livreRepo.existsById(id)) {
			feedbackRepo.findAllFeedbacksByLivreReference(id).forEach(feedback -> {
				if (!feedback.isArchiver())
					feedbacks.add(feedback);
			});
		}
		return feedbacks;
	}

//	--------------ADD	
	public Response<Boolean> addFeebacks(Long id, String userName, Feedback feedback) {
		if (livreRepo.existsById(id) && livreRepo.findById(id).get().isDisponibilite()) {
			if (!checkUserFeedBack(feedback.getCommentaire())) {
				User user = userRepo.findByUsername(userName).get();
				double total = 0, count = 0;
				Livre livre = livreRepo.findById(id).get();
				List<Feedback> oldFeedbacks = livre.getFeedbacks();
				oldFeedbacks.add(feedback);
				for (Feedback feedback2 : oldFeedbacks) {
					if (feedback2.getNote() != 0) {
						total += feedback2.getNote();
						count++;
					}
				}
				livre.setNote( (double) Math.round(total/count));

				livre.setFeedbacks(oldFeedbacks);
				livre.setNbComment(oldFeedbacks.size());

				feedback.setLivre(livre);
				feedback.setUser(user);
				feedbackRepo.save(feedback);

				livreRepo.save(livre);

//				add
				setFeedbackStat(feedback,true, false);

				return new ResponseService<Boolean>().getSuccessResponse(true);
			} else {
				setFeedbackStat(feedback,true, true);
				return new ResponseService<Boolean>()
						.getFailedResponse("u're comment is rejected because he contain bad words ....! ");
			}

		} else
			return new ResponseService<Boolean>().getFailedResponse("this book does not exist ...");
	}

	private void setFeedbackStat(Feedback feedback,boolean isBook, boolean rejected) {
		FeedBackStat feedBackStat = new FeedBackStat();
		if (feedbackStatRepo.findByDate(LocalDate.now()) != null)
			feedBackStat = feedbackStatRepo.findByDate(LocalDate.now());
		if (!rejected) {
			if(isBook)
			{
				if (positiveOrNegativeFeedback(negativeWord, feedback.getCommentaire()))
					feedBackStat.setNbNegativeCommentsBook(feedBackStat.getNbNegativeCommentsBook() + 1);
				else
					feedBackStat.setNbPositiveCommentsBook(feedBackStat.getNbPositiveCommentsBook() + 1);
			}else
			{
				if (positiveOrNegativeFeedback(negativeWord, feedback.getCommentaire()))
					feedBackStat.setNbNegativeCommentsOffer(feedBackStat.getNbNegativeCommentsOffer() + 1);
				else
					feedBackStat.setNbPositiveCommentsOffer(feedBackStat.getNbPositiveCommentsOffer() + 1);
			}
			
		} else
		{
			if(isBook)
				feedBackStat.setNbRejectedCommentsBook(feedBackStat.getNbRejectedCommentsBook() + 1);
			else
				feedBackStat.setNbRejectedCommentsOffer(feedBackStat.getNbRejectedCommentsOffer() + 1);
		}
			

		feedbackStatRepo.save(feedBackStat);

	}

	private boolean checkUserFeedBack(String comment) {
		boolean containBadWord = false;
		for (String bad : badWord) {
			if (comment.contains(bad)) {
				containBadWord = true;
				break;
			}
		}

		return containBadWord;
	}

	private Boolean positiveOrNegativeFeedback(ArrayList<String> list, String comment) {
		boolean containPositiveOrNegativeWord = false;
		for (String positive : list) {
			if (comment.contains(positive)) {
				containPositiveOrNegativeWord = true;
				break;
			}
		}
		return containPositiveOrNegativeWord;
	}

//	--------------UPDATE	
	public Response<Boolean> updateUserFeedback(Long id, String username, Feedback feedback) {
		if (livreRepo.existsById(id) && livreRepo.findById(id).get().isDisponibilite()) {
			Feedback feedback2 = feedbackRepo.findById(feedback.getId()).get();
			Livre livre = livreRepo.findById(id).get();
			if (feedback2.getUser().getUsername().equals(username)) {
				if (!feedback.getCommentaire().isEmpty() && !checkUserFeedBack(feedback.getCommentaire())) {
//					updated
					feedback2.setArchiver(true);

//					feedback2.setCommentaire(feedback.getCommentaire());
					feedback.setId(null);
					feedback.setLivre(feedback2.getLivre());
					feedback.setUser(feedback2.getUser());
					feedback.setArchiver(false);
					feedback.setNote(
							(feedback.getNote() != feedback2.getNote()) && feedback.getNote() != -1 ? feedback.getNote()
									: feedback2.getNote());
					feedbackRepo.save(feedback);
					feedbackRepo.save(feedback2);

					livre.setFeedbacks(updateUserOldFeedback(livre.getFeedbacks(), feedback2.getId(), feedback));
					livreRepo.save(livre);

					User user = userRepo.findByUsername(username).get();
					user.setFeedbacks(updateUserOldFeedback(user.getFeedbacks(), feedback2.getId(), feedback));
					userRepo.save(user);
					setFeedbackStat(feedback,true,false);
					return new ResponseService<Boolean>().getSuccessResponse(true);
				} else
				{
					setFeedbackStat(feedback,true,true);
					return new ResponseService<Boolean>()
							.getFailedResponse("u're comment is rejected because he contain bad words ....! ");
				}
					

			} else
				return new ResponseService<Boolean>()
						.getFailedResponse("Sorry , this comment doesn't belong to you ...");
		} else
			return new ResponseService<Boolean>().getFailedResponse("this book does not exist ...");
	}

	private List<Feedback> updateUserOldFeedback(List<Feedback> feedbacks, Long oldFeedbackId, Feedback userFeedback) {
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getId() == oldFeedbackId)
				feedbacks.remove(i);
//				feedbacks.set(i, userFeedback);
		}
		feedbacks.add(userFeedback);
		return feedbacks;
	}

//	--------------DELETE
	public Response<Boolean> deleteAllFeedBackOfOneBook(Long id) {
		if (livreRepo.existsById(id)) {
			Livre livre = livreRepo.findById(id).get();
			livre.setFeedbacks(null);
			livre.setNote(0);
			livre.setNbComment(0);
			livreRepo.save(livre);
			feedbackRepo.findAll().forEach(feedback -> {
				feedback.setArchiver(true);
				feedbackRepo.save(feedback);
			});
//			feedbackRepo.deleteAll();

			return new ResponseService<Boolean>().getSuccessResponse(true);
		} else
			return new ResponseService<Boolean>().getFailedResponse("this book does not exist ...");
	}

	public Response<Boolean> deleteUserComment(Long livreId, String username, Feedback feedback) {
		if (livreRepo.existsById(livreId) && livreRepo.findById(livreId).get().isDisponibilite()) {
			Feedback feedback2 = feedbackRepo.findById(feedback.getId()).get();
			Livre livre = livreRepo.findById(livreId).get();
			User user = userRepo.findByUsername(username).get();
			if (feedback2 != null && feedback2.getUser().getUsername().equals(username)
					&& user.getFeedbacks().size() > 0 && !feedback2.isArchiver()) {
				System.out.println(".............exist " + feedbackRepo.existsByLivreReference(livreId));
				if (feedbackRepo.existsByLivreReference(livreId)) {
					livre.setFeedbacks(RemoveFeebackOfBook(feedback2.getId(), livre.getFeedbacks()));
					livre.setNbComment(livre.getFeedbacks().size());
					livreRepo.save(livre);

					user.setFeedbacks(RemoveFeebackOfBook(feedback2.getId(), user.getFeedbacks()));
					userRepo.save(user);
					feedback2.setArchiver(true);
					feedbackRepo.save(feedback2);

					return new ResponseService<Boolean>().getSuccessResponse(true);
				} else
					return new ResponseService<Boolean>()
							.getFailedResponse("Sorry , this comment doesn't belong to this book ...");

			} else
				return new ResponseService<Boolean>()
						.getFailedResponse("Sorry , this comment doesn't belong to you ...");
		} else
			return new ResponseService<Boolean>().getFailedResponse("this book does not exist ...");
	}

	private List<Feedback> RemoveFeebackOfBook(Long ref, List<Feedback> feedbacks) {
		for (int i = 0; i < feedbacks.size(); i++) {
			if (feedbacks.get(i).getId() == ref) {
				System.out.println("----index of =" + i);
				feedbacks.remove(i);
			}
		}

		return feedbacks;
	}

//	---------------------------------------------------OFFER
//	--------------READ
	public List<Feedback> findAllFeebackOfAOffer(Long id) {
		List<Feedback> feedbacks = new ArrayList<>();
		if (offreRepo.existsById(id))
			feedbackRepo.findAllFeedbacksByOffreReference(id).forEach(feedback -> {
				if (!feedback.isArchiver())
					feedbacks.add(feedback);
			});

		return feedbacks;
	}

//--------------ADD	
	public Response<Boolean> addFeebacksInOffer(Long id, String userName, Feedback feedback) {
		if (offreRepo.existsById(id) && offreRepo.findById(id).get().isDiponibilite()) {
			if (!checkUserFeedBack(feedback.getCommentaire())) {
				User user = userRepo.findByUsername(userName).get();
				double total = 0, count = 0;
				Offre offre = offreRepo.findById(id).get();
				List<Feedback> oldFeedbacks = offre.getFeedbacks();
				oldFeedbacks.add(feedback);
				for (Feedback feedback2 : oldFeedbacks) {
					if (feedback2.getNote() != 0) {
						total += feedback2.getNote();
						count++;
					}
				}
				offre.setNote((double) Math.round(total/count));

				offre.setFeedbacks(oldFeedbacks);
				offre.setNbComment(oldFeedbacks.size());

				feedback.setOffre(offre);
				feedback.setUser(user);
				feedbackRepo.save(feedback);

				offreRepo.save(offre);
				
				setFeedbackStat(feedback,false, false);
				return new ResponseService<Boolean>().getSuccessResponse(true);
			} else
			{
				setFeedbackStat(feedback,false, false);
				return new ResponseService<Boolean>()
						.getFailedResponse("u're comment is rejected because he contain bad words ....! ");
			}
				

		} else
			return new ResponseService<Boolean>().getFailedResponse("this Offer does not exist ...");
	}

//	--------------UPDATE
	public Response<Boolean> updateUserFeedbackOfAnOffer(Long id, String username, Feedback feedback) {
		if (offreRepo.existsById(id) && offreRepo.findById(id).get().isDiponibilite()) {
			Feedback feedback2 = feedbackRepo.findById(feedback.getId()).get();
			Offre offre = offreRepo.findById(id).get();
			if (feedback2.getUser().getUsername().equals(username)) {
				if (!feedback.getCommentaire().isEmpty() && !checkUserFeedBack(feedback.getCommentaire())) {
//					feedback2.setCommentaire(feedback.getCommentaire());

					feedback.setId(null);
					feedback.setOffre(feedback2.getOffre());
					feedback.setUser(feedback2.getUser());
					feedback.setArchiver(false);
					feedback.setNote(
							(feedback.getNote() != feedback2.getNote()) && feedback.getNote() != -1 ? feedback.getNote()
									: feedback2.getNote());

					feedback2.setArchiver(true);
					feedbackRepo.save(feedback2);
					feedbackRepo.save(feedback);

					offre.setFeedbacks(updateUserOldFeedback(offre.getFeedbacks(), feedback2.getId(), feedback));
					offreRepo.save(offre);

					User user = userRepo.findByUsername(username).get();
					user.setFeedbacks(updateUserOldFeedback(user.getFeedbacks(), feedback2.getId(), feedback));
					userRepo.save(user);
					setFeedbackStat(feedback,false,false);
					return new ResponseService<Boolean>().getSuccessResponse(true);
				} else {
					setFeedbackStat(feedback,false,true);
					return new ResponseService<Boolean>()
							.getFailedResponse("u're comment is rejected because he contain bad words ....! ");
				}
					

			} else
				return new ResponseService<Boolean>()
						.getFailedResponse("Sorry , this comment doesn't belong to you ...");
		} else
			return new ResponseService<Boolean>().getFailedResponse("this offer does not exist ...");
	}

//	--------------DELETE
	public Response<Boolean> deleteUserOfferComment(Long id, String username, Feedback feedback) {
		if (offreRepo.existsById(id) && offreRepo.findById(id).get().isDiponibilite()) {
			Feedback feedback2 = feedbackRepo.findById(feedback.getId()).get();
			Offre offre = offreRepo.findById(id).get();
			User user = userRepo.findByUsername(username).get();
			if (feedback2 != null && feedback2.getUser().getUsername().equals(username)
					&& user.getFeedbacks().size() > 0) {
				if (feedbackRepo.existsByOffreReference(id)) {
					offre.setFeedbacks(RemoveFeebackOfBook(feedback2.getId(), offre.getFeedbacks()));
					offre.setNbComment(offre.getFeedbacks().size());
					offreRepo.save(offre);

					user.setFeedbacks(RemoveFeebackOfBook(feedback2.getId(), user.getFeedbacks()));
					userRepo.save(user);

					feedback.setArchiver(true);
					feedbackRepo.save(feedback);
//					feedbackRepo.deleteById(feedback2.getId());

					return new ResponseService<Boolean>().getSuccessResponse(true);
				} else
					return new ResponseService<Boolean>()
							.getFailedResponse("Sorry , this comment doesn't belong to the offer ...");

			} else
				return new ResponseService<Boolean>()
						.getFailedResponse("Sorry , this comment doesn't belong to you ...");
		} else
			return new ResponseService<Boolean>().getFailedResponse("this offer does not exist ...");
	}

}