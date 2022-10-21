package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.repository.FamilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamilleService {


    private final FamilleRepository familleRepository;

    @Autowired
    public FamilleService(FamilleRepository familleRepository) {
        this.familleRepository = familleRepository;
    }

    public List<Famille> getAllFamilles() {
        return familleRepository.findAll();
    }

    public Famille getFamilleByEmail(String email) {
        return familleRepository.findById(email).get();
    }

    public void updateFamille(Famille famille) {
        familleRepository.save(famille);
    }
}
