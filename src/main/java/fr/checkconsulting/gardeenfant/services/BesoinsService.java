package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.dto.BesoinsDTO;
import fr.checkconsulting.gardeenfant.entity.Besoins;
import fr.checkconsulting.gardeenfant.repository.BesoinsRepository;
import fr.checkconsulting.gardeenfant.security.CommonData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
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
        String emailFamille = CommonData.getEmail();
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

    //Ajouter un nouveau besoin
    public void creerNouveauBesoin(Besoins besoin) throws Exception {
        try{
            besoinsRepository.save(besoin);
        } catch (DataAccessException e) {
            throw new Exception("L'insertion de données a échouée");
        }
    }

    //Modifier un besoin
    public void modifierBesoin(Besoins besoin) throws Exception {
        try{
            besoinsRepository.save(besoin);
        } catch (DataAccessException e) {
            throw new Exception("La modification a échouée");
        }
    }

    //Supprimer un besoin
    public void supprimerBesoin(String id) throws Exception {
        try{
            besoinsRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new Exception("La suppression a échouée");
        }
    }

    //Supprimer tous les besoins
    @Transactional
    public void supprimerTousBesoins() throws Exception {
        String emailFamille = CommonData.getEmail();
        try{
            besoinsRepository.deleteByEmailFamille(emailFamille);
        } catch (DataAccessException e) {
            throw new Exception("La suppression a échouée : ", e);
        }
    }
}
