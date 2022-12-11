package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Besoins;
import fr.checkconsulting.gardeenfant.entity.Famille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, String> {
    //    @Query("select distinct f from Famille f inner join Besoins b on f.email=b.emailFamille " +
//            "where (:nom is null or lower(f.nom) = lower(:nom)) " +
//            "and (:prenom is null or lower(f.prenomRepresentant) = lower(:prenom)) " +
//            "and (:ville is null or lower(f.ville) = lower(:ville)) " +
//            "and (:jour = -1 or b.jour = :jour) " +
//            "and (cast(:matinDebut as time) is null or b.besoin_matin_debut <= cast(:matinDebut as time)) " +
//            "and (cast(:matinFin as time) is null or cast(:matinFin as time) <= b.besoin_matin_fin) " +
//            "and (cast(:midiDebut as time) is null or b.besoin_midi_debut <= cast(:midiDebut as time)) " +
//            "and (cast(:midiFin as time) is null or cast(:midiFin as time) <= b.besoin_midi_fin) " +
//            "and (cast(:soirDebut as time) is null or b.besoin_soir_debut <= cast(:soirDebut as time)) " +
//            "and (cast(:soirFin as time) is null or cast(:soirFin as time) <= b.besoin_soir_fin) "
//    )
//    List<Famille> getFamillesByCriteria(
//            @Param("nom") String nom,
//            @Param("prenom") String prenom,
//            @Param("ville") String ville,
//            @Param("jour") int jour,
//            @Param("matinDebut") LocalTime matinDebut,
//            @Param("matinFin") LocalTime matinFin,
//            @Param("midiDebut") LocalTime midiDebut,
//            @Param("midiFin") LocalTime midiFin,
//            @Param("soirDebut") LocalTime soirDebut,
//            @Param("soirFin") LocalTime soirFin
//    );
//    @Query("select f from Famille f where (:nom is null or f.nom = :nom) and (:prenom is null or f.prenomRepresentant = :prenom) and (:ville is null or f.ville = :ville)")
//    List<Famille> getFamillesByCriteria(@Param("nom") String nom, @Param("prenom") String prenom, @Param("ville") String ville);
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
