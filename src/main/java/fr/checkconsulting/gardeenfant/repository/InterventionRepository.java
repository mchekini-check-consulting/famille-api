package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, String> {
    //@Query("SELECT int, COUNT(int.timeIntervention) FROM Intervention int GROUP BY int.emailNounou ORDER BY int.emailNounou ASC")
    List<Intervention> findAll();
}
