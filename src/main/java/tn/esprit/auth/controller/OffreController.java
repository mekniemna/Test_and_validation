package tn.esprit.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tn.esprit.auth.entity.Livre;
import tn.esprit.auth.entity.Offre;
import tn.esprit.auth.model.Response;
import tn.esprit.auth.service.OffreService;


@RestController
@RequestMapping("/offre")
public class OffreController {
//	CRUD : 
//	- add ( offre / offre + [livres] ) 
//	- update [ livre(ajout/supp) + info ] **
//	- delete [deleteById* / deleteAll* / deleteAllBooks* /deleteBook] **
//	- READ [all* / byId* / allBook* / bookById*] **
	
	@Autowired
	private OffreService service;
	
	
//	------------------READ
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("")
	public List<Offre> findAll(){
		return service.findAll();
	}
	
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/{offreId}")
	public Response<Offre> findById(@PathVariable Long offreId)
	{
		return service.findById(offreId);
	}
	
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/{offreId}/livres")
	public List<Livre> findAllBooks(@PathVariable Long offreId)
	{
		return service.findAllBooks(offreId);
	}
	
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN')")
	@GetMapping("/{offreId}/livre/{livreId}")
	public Response<Livre> findBookById(@PathVariable Long offreId, @PathVariable Long livreId )
	{
		return service.findBookById(offreId);
	}
	
	
//	------------------ADD
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@PostMapping("")
	public Offre save(@RequestBody Offre offre)
	{
		return service.save(offre);
	}
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@PostMapping("/{offreId}/livre/{livreId}")
	public Response<Offre> saveBook(@PathVariable Long offreId ,@PathVariable Long livreId )
	{
		return service.saveBook(livreId , offreId);	
	}
	
	
//	------------------UPDATE 
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@PutMapping("/{offreId}")
	public Response<Offre> updateOffreById(@PathVariable Long offreId,@RequestBody Offre offre)
	{
		return service.update(offre, offreId);
	}
	
	
//	------------------DELETE
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@DeleteMapping("/{offreId}")
	public Response<Boolean> deleteById(@PathVariable Long offreId)
	{
		return service.deleteById(offreId);
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@DeleteMapping("/{offreId}/livres")
	public Response<Boolean> deleteAllBooks(@PathVariable Long offreId)
	{
		return service.deleteBooks(offreId);
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@DeleteMapping("/{offreId}/livre/{bookId}")
	public Response<Boolean> deleteBookById(@PathVariable Long offreId,@PathVariable Long bookId)
	{
		return service.deleteBookById(offreId,bookId);
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGEMENT') or hasRole('ROLE_ADMIN') ")
	@DeleteMapping("/all")
	public Response<Boolean> deleteAll() {
		return service.deleteAll();
	}
	

}

