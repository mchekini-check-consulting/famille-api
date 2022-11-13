package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.repository.FamilleRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public Famille getFamilleByEmail() throws Exception {
        Jwt user =  ((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        String email = String.valueOf(user.getClaims().get("email"));
        log.info("Email: " + email);
        Optional<Famille> result = familleRepository.findById(email);
        if(result.isPresent()) {
            return result.get();
        }else {
            throw new Exception("La requête a échouée");
        }
    }

    public void updateFamille(Famille famille) throws Exception {
        try{
            familleRepository.save(famille);
        } catch (DataAccessException e) {
            throw new Exception("La modification de données a échouée");
        }
    }
}