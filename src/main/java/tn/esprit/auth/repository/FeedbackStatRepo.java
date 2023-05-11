package tn.esprit.auth.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.auth.entity.FeedBackStat;
@Repository
public interface FeedbackStatRepo extends CrudRepository<FeedBackStat, Long>{
	
	public FeedBackStat findByDate(LocalDate date);
	
	public List<FeedBackStat> findAllByDate(LocalDate date);
	
	public List<FeedBackStat> findAll();

}
