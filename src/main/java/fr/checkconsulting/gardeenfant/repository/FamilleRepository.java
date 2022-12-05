package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Famille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, String> {
    @Query("select f from Famille f where (:nom is null or f.nom = :nom) and (:prenom is null or f.prenomRepresentant = :prenom) and (:ville is null or f.ville = :ville)")
    List<Famille> getFamillesByCriteria(@Param("nom") String nom, @Param("prenom") String prenom, @Param("ville") String ville);
}
