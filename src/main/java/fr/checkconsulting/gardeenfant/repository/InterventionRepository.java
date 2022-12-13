package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, String> {
    List<Intervention> findAllByEmailFamille(String emailFamille);

    List<Intervention> findAllByEmailNounou(String emailNounou);

    @Modifying
    @Query("update Intervention i set i.etat = 'Annulée' where (i.emailFamille = :emailFamille and i.emailNounou = :emailNounou)")
    @Transactional
    void cancelIntervention(@Param("emailFamille") String emailFamille, @Param("emailNounou") String emailNounou);

    @Modifying
    @Query("update Intervention i set i.etat = 'Instance' where (i.emailFamille = :emailFamille and i.emailNounou = :emailNounou)")
    @Transactional
    void reviveIntervention(@Param("emailFamille") String emailFamille, @Param("emailNounou") String emailNounou);

    @Modifying
    @Query("update Intervention i set i.etat = 'Rejetée' where (i.emailFamille = :emailFamille and i.emailNounou = :emailNounou)")
    @Transactional
    void rejectIntervention(@Param("emailFamille") String emailFamille, @Param("emailNounou") String emailNounou);

    @Modifying
    @Query("update Intervention i set i.etat = 'Confirmée' where (i.emailFamille = :emailFamille and i.emailNounou = :emailNounou)")
    @Transactional
    void acceptIntervention(@Param("emailFamille") String emailFamille, @Param("emailNounou") String emailNounou);
}
