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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class BesoinsService {
    private final ModelMapper modelMapper;
    private final BesoinsRepository besoinsRepository;

    String timeColonPattern = "HH:mm:ss";
    DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);

    @Autowired
    public BesoinsService(BesoinsRepository besoinsRepository, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.besoinsRepository = besoinsRepository;
    }

    //Afficher tous les besoins crées par une famille
    @SneakyThrows
    public List<Besoins> getAllBesoinsByEmailFamille() throws Exception {
        String emailFamille = CommonData.getEmail();
        List<Besoins> result = besoinsRepository
                .findAllByEmailFamille(emailFamille)
                .stream().map(besoin -> modelMapper.map(besoin, Besoins.class))
                .collect(Collectors.toList());
        if(!result.isEmpty()) {
            return result;
        }else {
            throw new Exception("La requête a échouée");
        }
    }

    public Besoins getAllBesoinsByEmailAndJour(int jour) {
        String emailFamille = CommonData.getEmail();
        return besoinsRepository
                .findAllByEmailFamilleAndJour(emailFamille, jour);
    }

    //Ajouter un nouveau besoin
    public void creerNouveauBesoin(BesoinsDTO besoin) throws Exception {
        // Convertir les champs de type number to localTime

        LocalTime matin_debut = besoin.getBesoin_matin_debut() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_matin_debut(), 0,0)));
        LocalTime matin_fin = besoin.getBesoin_matin_debut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_matin_fin(), 0,0)));
        LocalTime midi_debut = besoin.getBesoin_midi_debut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_midi_debut(), 0,0)));
        LocalTime midi_fin = besoin.getBesoin_midi_fin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_midi_fin(), 0,0)));
        LocalTime soir_debut = besoin.getBesoin_soir_debut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_soir_debut(), 0,0)));
        LocalTime soir_fin = besoin.getBesoin_soir_fin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_soir_fin(), 0,0)));

        Besoins besoinData = new Besoins();

        besoinData.setId_besoin(besoin.getId_besoin());
        besoinData.setJour(besoin.getJour());
        besoinData.setBesoin_matin_debut(matin_debut);
        besoinData.setBesoin_matin_fin(matin_fin);
        besoinData.setBesoin_midi_debut(midi_debut);
        besoinData.setBesoin_midi_fin(midi_fin);
        besoinData.setBesoin_soir_debut(soir_debut);
        besoinData.setBesoin_soir_fin(soir_fin);
        besoinData.setEmailFamille(CommonData.getEmail());
        try{
            besoinsRepository.save(besoinData);
        } catch (DataAccessException e) {
            throw new Exception("L'insertion de données a échouée");
        }
    }

    //Modifier un besoin
    public void modifierBesoin(BesoinsDTO besoin) throws Exception {
        // Convertir les champs de type number to localTime
        Besoins besoinData = new Besoins();
        besoinData.setId_besoin(besoin.getId_besoin());
        besoinData.setJour(besoin.getJour());
        // Récupèrer le contenu persisté dans la BDD
        Besoins data = getAllBesoinsByEmailAndJour(besoin.getJour());
        besoinData.setEmailFamille(data.getEmailFamille());
        switch (besoin.getType()){
            case "matin":
                LocalTime matin_debut = besoin.getBesoin_matin_debut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_matin_debut(), 0,0)));
                LocalTime matin_fin = besoin.getBesoin_matin_fin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_matin_fin(), 0,0)));
                besoinData.setBesoin_matin_debut(matin_debut);
                besoinData.setBesoin_matin_fin(matin_fin);
                besoinData.setBesoin_midi_debut(data.getBesoin_midi_debut());
                besoinData.setBesoin_midi_fin(data.getBesoin_midi_fin());
                besoinData.setBesoin_soir_debut(data.getBesoin_soir_debut());
                besoinData.setBesoin_soir_fin(data.getBesoin_soir_fin());
                break;
            case "midi":
                LocalTime midi_debut = besoin.getBesoin_midi_debut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_midi_debut(), 0,0)));
                LocalTime midi_fin = besoin.getBesoin_midi_fin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_midi_fin(), 0,0)));
                besoinData.setBesoin_midi_debut(midi_debut);
                besoinData.setBesoin_midi_fin(midi_fin);
                besoinData.setBesoin_matin_debut(data.getBesoin_matin_debut());
                besoinData.setBesoin_matin_fin(data.getBesoin_matin_fin());
                besoinData.setBesoin_soir_debut(data.getBesoin_soir_debut());
                besoinData.setBesoin_soir_fin(data.getBesoin_soir_fin());
                break;
            case "soir":
                LocalTime soir_debut = besoin.getBesoin_soir_debut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_soir_debut(), 0,0)));
                LocalTime soir_fin = besoin.getBesoin_soir_fin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoin_soir_fin(), 0,0)));
                besoinData.setBesoin_soir_debut(soir_debut);
                besoinData.setBesoin_soir_fin(soir_fin);
                besoinData.setBesoin_matin_debut(data.getBesoin_matin_debut());
                besoinData.setBesoin_matin_fin(data.getBesoin_matin_fin());
                besoinData.setBesoin_midi_debut(data.getBesoin_midi_debut());
                besoinData.setBesoin_midi_fin(data.getBesoin_midi_fin());
                break;
            default:
                log.info("Erreur inattendue !");
        }
        try{
            besoinsRepository.save(besoinData);
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
