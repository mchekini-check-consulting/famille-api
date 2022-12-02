package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Options;
import fr.checkconsulting.gardeenfant.entity.OptionsDTO;
import fr.checkconsulting.gardeenfant.repository.OptionsRepository;
import fr.checkconsulting.gardeenfant.security.CommonData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;

    @Autowired
    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    @SneakyThrows
    public Options getOptionsFamilleById() throws Exception {
        String email = CommonData.getEmail();
        Optional<Options> result = optionsRepository.findById(email);
        if(result.isPresent()) {
            return result.get();
        }else {
            throw new Exception("La requête a échouée");
        }
    }

    public void updateOptionsFamille(OptionsDTO options) throws Exception {
        String email = CommonData.getEmail();
        Options data = new Options();
        data.setEmailFamille(email);
        data.setAutosave(options.autosave ? 1 : 0);
        try{
            optionsRepository.save(data);
        } catch (DataAccessException e) {
            throw new Exception("La modification de données a échouée: \n" + e);
        }
    }
}