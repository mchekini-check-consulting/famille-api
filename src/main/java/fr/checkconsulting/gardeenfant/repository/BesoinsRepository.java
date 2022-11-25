package fr.checkconsulting.gardeenfant.repository;


import fr.checkconsulting.gardeenfant.entity.Besoins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinsRepository extends JpaRepository<Besoins, String> {
    List<Besoins> findAllByEmailFamille(String emailFamille);
    Besoins findAllByEmailFamilleAndJour(String emailFamille, int jour);
    void deleteByEmailFamille(String emailFamille);


}
