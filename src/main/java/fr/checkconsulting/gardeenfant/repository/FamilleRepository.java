package fr.checkconsulting.gardeenfant.repository;

import fr.checkconsulting.gardeenfant.entity.Famille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, String> {
}
