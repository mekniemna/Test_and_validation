package tn.esprit.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.auth.entity.Livre;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long>{
	public Livre findByOffreReference(Long id);
	public List<Livre> findAllByOffreReference(Long id);
	public Boolean existsByOffreReference(Long id);
	public List<Livre> findAll();
	public List<Livre> findAllByDisponibilite(boolean disponibilite);
}
