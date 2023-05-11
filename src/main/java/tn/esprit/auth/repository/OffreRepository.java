package tn.esprit.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.auth.entity.Offre;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long>{
	public List<Offre> findAll();
	
	public List<Offre> findAllByDiponibilite(boolean disponibilite);
}
