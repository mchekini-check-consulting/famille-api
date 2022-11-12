package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.dto.BesoinsDTO;
import fr.checkconsulting.gardeenfant.repository.BesoinsRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @SneakyThrows
    public List<BesoinsDTO> getAllBesoinsByEmailFamille(String emailFamille) throws Exception {
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
