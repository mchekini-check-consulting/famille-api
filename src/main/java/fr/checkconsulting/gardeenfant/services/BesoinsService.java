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

        LocalTime matin_debut = besoin.getBesoinMatinDebut() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMatinDebut(), 0,0)));
        LocalTime matin_fin = besoin.getBesoinMatinFin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMatinFin(), 0,0)));
        LocalTime midi_debut = besoin.getBesoinMidiDebut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMidiDebut(), 0,0)));
        LocalTime midi_fin = besoin.getBesoinMidiFin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMidiFin(), 0,0)));
        LocalTime soir_debut = besoin.getBesoinSoirDebut() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinSoirDebut(), 0,0)));
        LocalTime soir_fin = besoin.getBesoinSoirFin() == null ? null :LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinSoirFin(), 0,0)));

        Besoins besoinData = new Besoins();

        besoinData.setIdBesoin(besoin.getIdBesoin());
        besoinData.setJour(besoin.getJour());
        besoinData.setBesoinMatinDebut(matin_debut);
        besoinData.setBesoinMatinFin(matin_fin);
        besoinData.setBesoinMidiDebut(midi_debut);
        besoinData.setBesoinMidiFin(midi_fin);
        besoinData.setBesoinSoirDebut(soir_debut);
        besoinData.setBesoinSoirFin(soir_fin);
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
        besoinData.setIdBesoin(besoin.getIdBesoin());
        besoinData.setJour(besoin.getJour());
        // Récupèrer le contenu persisté dans la BDD
        Besoins data = getAllBesoinsByEmailAndJour(besoin.getJour());

        besoinData.setEmailFamille(CommonData.getEmail());

        LocalTime matin_debut = data == null ? null : data.getBesoinMatinDebut();
        LocalTime matin_fin = data == null ? null : data.getBesoinMatinFin();
        LocalTime midi_debut = data == null ? null : data.getBesoinMidiDebut();
        LocalTime midi_fin = data == null ? null : data.getBesoinMidiFin();
        LocalTime soir_debut = data == null ? null : data.getBesoinSoirDebut();
        LocalTime soir_fin = data == null ? null : data.getBesoinSoirFin();

        switch (besoin.getType()){
            case "mat":
                matin_debut = besoin.getBesoinMatinDebut() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMatinDebut(), 0,0)));
                matin_fin = besoin.getBesoinMatinFin() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMatinFin(), 0,0)));
                besoinData.setBesoinMatinDebut(matin_debut);
                besoinData.setBesoinMatinFin(matin_fin);
                besoinData.setBesoinMidiDebut(midi_debut);
                besoinData.setBesoinMidiFin(midi_fin);
                besoinData.setBesoinSoirDebut(soir_debut);
                besoinData.setBesoinSoirFin(soir_fin);
                break;
            case "mid":
                midi_debut = besoin.getBesoinMidiDebut() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMidiDebut(), 0,0)));
                midi_fin = besoin.getBesoinMidiFin() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinMidiFin(), 0,0)));
                besoinData.setBesoinMidiDebut(midi_debut);
                besoinData.setBesoinMidiFin(midi_fin);
                besoinData.setBesoinMatinDebut(matin_debut);
                besoinData.setBesoinMatinFin(matin_fin);
                besoinData.setBesoinSoirDebut(soir_debut);
                besoinData.setBesoinSoirFin(soir_fin);
                break;
            case "soi":
                soir_debut = besoin.getBesoinSoirDebut() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinSoirDebut(), 0,0)));
                soir_fin = besoin.getBesoinSoirFin() == null ? null : LocalTime.parse(timeColonFormatter.format(LocalTime.of(besoin.getBesoinSoirFin(), 0,0)));
                besoinData.setBesoinSoirDebut(soir_debut);
                besoinData.setBesoinSoirFin(soir_fin);
                besoinData.setBesoinMatinDebut(matin_debut);
                besoinData.setBesoinMatinFin(matin_fin);
                besoinData.setBesoinMidiDebut(midi_debut);
                besoinData.setBesoinMidiFin(midi_fin);
                break;
            default:
                log.info("Erreur inattendue !");
        }
        try{
            besoinsRepository.save(besoinData);

            // Vérifier si tous le besoins de la journée sont annulés (mis à null)
            if(data != null && matin_debut == null && matin_fin == null && midi_debut == null && midi_fin == null && soir_debut == null && soir_fin == null) {
                supprimerBesoin(besoin.getIdBesoin());
            }
        } catch (DataAccessException e) {
            throw new Exception("La modification a échouée");
        }
    }


    @Transactional
    public void modifierTousBesoin(BesoinsDTO[] besoins) throws Exception {
        for (BesoinsDTO besoin : besoins) {
            modifierBesoin(besoin);
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
