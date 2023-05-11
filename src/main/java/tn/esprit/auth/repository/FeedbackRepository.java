package tn.esprit.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.auth.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
	
	public List<Feedback> findAllFeedbacksByLivreReference(Long id);	
	public List<Feedback> findAllFeedbacksByOffreReference(Long id);
	
	public boolean existsByLivreReference(Long id);
	public boolean existsByOffreReference(Long id);

}
