package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.repository.FamilleRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @SneakyThrows
    public Famille getFamilleByEmail(String email) {
        Optional<Famille> result = familleRepository.findById(email);
        if(result.isPresent()) {
            return result.get();
        }else {
            throw new ResourceNotFoundException("Famille", "email", email);
        }
    }

    public void updateFamille(Famille famille) {
        familleRepository.save(famille);
    }
}
