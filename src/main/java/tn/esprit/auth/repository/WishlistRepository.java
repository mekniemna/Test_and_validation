package tn.esprit.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.auth.entity.WishList;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {

}
