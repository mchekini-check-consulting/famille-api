package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.dto.BesoinsDTO;
import fr.checkconsulting.gardeenfant.repository.BesoinsRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BesoinsService {
    private final ModelMapper modelMapper;
    private final BesoinsRepository besoinsRepository;

    @Autowired
    public BesoinsService(BesoinsRepository besoinsRepository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.besoinsRepository = besoinsRepository;
    }

    //Afficher tous les besoins crées par une famille
    @SneakyThrows
    public List<BesoinsDTO> getAllBesoinsByEmailFamille() throws Exception {
        Jwt user =  ((Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        String emailFamille = String.valueOf(user.getClaims().get("email"));
        List<BesoinsDTO> result = besoinsRepository
                .findAllByEmailFamille(emailFamille)
                .stream().map(besoin -> modelMapper.map(besoin, BesoinsDTO.class))
                .collect(Collectors.toList());
        if(!result.isEmpty()) {
            return result;
        }else {
            throw new Exception("La requête a échouée");
        }
    }
}
