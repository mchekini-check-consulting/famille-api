package fr.checkconsulting.gardeenfant.repository;


import fr.checkconsulting.gardeenfant.entity.Besoins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface BesoinsRepository extends JpaRepository<Besoins, String> {
    List<Besoins> findAllByEmailFamille(String emailFamille);
    Besoins findAllByEmailFamilleAndJour(String emailFamille, int jour);
    void deleteByEmailFamille(String emailFamille);
    @Query("select b from Besoins b where (:jour = -1 or b.jour = :jour)")
    List<Besoins> findAllByJour(@Param("jour") int jour);
}
