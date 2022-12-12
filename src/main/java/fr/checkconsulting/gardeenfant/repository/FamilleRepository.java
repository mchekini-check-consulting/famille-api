package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Famille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, String> {
   List<Famille> findAll();
    @Query("select distinct f " +
            "from Famille f " +
            "inner join Besoins b on f.email = b.emailFamille " +
            "where (:nom is null or lower(f.nom) = lower(:nom)) " +
            "and (:prenom is null or lower(f.prenomRepresentant) = lower(:prenom)) " +
            "and (:ville is null or lower(f.ville) = lower(:ville))")
    List<Famille> getFamillesByCriteria(
            @Param("nom") String nom,
            @Param("prenom") String prenom,
            @Param("ville") String ville
    );
}
