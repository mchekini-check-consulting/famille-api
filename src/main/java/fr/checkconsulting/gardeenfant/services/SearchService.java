package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.dto.DisponibiliteDTO;
import fr.checkconsulting.gardeenfant.dto.FamilleDTO;
import fr.checkconsulting.gardeenfant.dto.NounouDto;
import fr.checkconsulting.gardeenfant.entity.Besoins;
import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.repository.BesoinsRepository;
import fr.checkconsulting.gardeenfant.repository.FamilleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Value("${nounou.application.url}")
    String nounouUrl;

    private final FamilleRepository familleRepository;
    private final BesoinsRepository besoinsRepository;
    private final RestTemplate restTemplate;

    public SearchService(FamilleRepository familleRepository, BesoinsRepository besoinsRepository, RestTemplate restTemplate) {
        this.familleRepository = familleRepository;
        this.besoinsRepository = besoinsRepository;
        this.restTemplate = restTemplate;
    }

    public List<NounouDto> getNounouByCriteria(String nom, String prenom, String ville, int jour, String heureDebut, String heureFin) {

        String url = nounouUrl + "/api/v1/search/nounou?nom=" + nom + "&prenom=" + prenom + "&ville=" + ville + "&jour=" + jour + "&heureDebut=" + heureDebut + "&heureFin=" + heureFin;

        ResponseEntity<NounouDto[]> nounouDtos = restTemplate.getForEntity(url, NounouDto[].class);

        Arrays.stream(nounouDtos.getBody()).forEach(nounouDto -> {
            nounouDto.setAdresse(String.join(" ", nounouDto.getRue(), nounouDto.getVille(), nounouDto.getCodePostal()));
            nounouDto.setTelephone(nounouDto.getNumeroTelephone());
        });
        return Arrays.stream(nounouDtos.getBody()).collect(Collectors.toList());
    }

    public List<DisponibiliteDTO> getDispoNounou(String email) {

        String url = nounouUrl + "/api/v1/search/dispo-nounou/" + email;
        ResponseEntity<DisponibiliteDTO[]> dispoNounous = restTemplate.getForEntity(url, DisponibiliteDTO[].class);

        return Arrays.stream(dispoNounous.getBody()).collect(Collectors.toList());
    }

    public List<FamilleDTO> getFamilleByCriteria(String nom, String prenom, String ville, int jour, String heureDebut, String heureFin) {

        if ("".equals(nom)) nom = null;
        if ("".equals(prenom)) prenom = null;
        if ("".equals(ville)) ville = null;
        if ("".equals(heureDebut)) heureDebut = null;
        if ("".equals(heureFin)) heureFin = null;

        List<Famille> familles = familleRepository.getFamillesByCriteria(nom, prenom, ville);
        List<Besoins> besoins = besoinsRepository.findAllByJour(jour);

        if (jour != -1) {
            familles.removeIf(famille -> besoins.stream().noneMatch(besoin -> besoin.getEmailFamille().equals(famille.getEmail())));
        }
        if (heureDebut != null && heureFin != null) {
            Map<String, List<List<LocalTime>>> mappedBesoins = mapBesoins(besoins);
            List<String> emails = filterBesoinsByTimeInterval(mappedBesoins, LocalTime.parse(heureDebut), LocalTime.parse(heureFin));
            familles.removeIf(famille -> !emails.contains(famille.getEmail()));
        }

        List<FamilleDTO> famillesDto = new ArrayList<>();
        familles.forEach(famille -> {

            FamilleDTO familleDTO = FamilleDTO.builder()
                    .nom(famille.getNom())
                    .prenom(famille.getPrenomRepresentant())
                    .adresse(String.join(" ", famille.getRue(), famille.getCodePostal(), famille.getVille()))
                    .mail(famille.getEmail())
                    .telephone(famille.getNumeroTelephone())
                    .build();

            famillesDto.add(familleDTO);
        });


        return famillesDto;
    }

    private List<String> filterBesoinsByTimeInterval(Map<String, List<List<LocalTime>>> besoins, LocalTime heureDebut, LocalTime heureFin) {
        List<String> emails = new ArrayList<>();
        for (Map.Entry<String, List<List<LocalTime>>> besoin : besoins.entrySet()) {
            boolean validTimeInterval = false;
            for (List<LocalTime> intervals : besoin.getValue()) {
                for (int i = 0; i < intervals.size(); i += 2) {
                    if (!intervals.get(i).isAfter(heureDebut) && !heureFin.isAfter(intervals.get(i + 1))) {
                        validTimeInterval = true;
                        break;
                    }
                }
                if (validTimeInterval) {
                    break;
                }
            }
            if (validTimeInterval) {
                emails.add(besoin.getKey());
            }
        }
        return emails;
    }

    private Map<String, List<List<LocalTime>>> mapBesoins(List<Besoins> besoins) {
        Map<String, List<List<LocalTime>>> mappingBesoins = new HashMap<>();
        for (Besoins besoin : besoins) {
            List<LocalTime> intervals = new ArrayList<>();
            if (besoin.getBesoin_matin_debut() != null) intervals.add(besoin.getBesoin_matin_debut());
            if (besoin.getBesoin_matin_fin() != null) intervals.add(besoin.getBesoin_matin_fin());
            if (besoin.getBesoin_midi_debut() != null) intervals.add(besoin.getBesoin_midi_debut());
            if (besoin.getBesoin_midi_fin() != null) intervals.add(besoin.getBesoin_midi_fin());
            if (besoin.getBesoin_soir_debut() != null) intervals.add(besoin.getBesoin_soir_debut());
            if (besoin.getBesoin_soir_fin() != null) intervals.add(besoin.getBesoin_soir_fin());
            if (intervals.size() == 6 && intervals.get(1).equals(intervals.get(2))) {
                intervals.remove(1);
                intervals.remove(1);
            }
            if (intervals.size() == 4 && intervals.get(1).equals(intervals.get(2))) {
                intervals.remove(1);
                intervals.remove(1);
            }
            List<List<LocalTime>> intervalsList = new ArrayList<>();
            if (mappingBesoins.containsKey(besoin.getEmailFamille())) {
                intervalsList = mappingBesoins.get(besoin.getEmailFamille());
            }
            intervalsList.add(intervals);
            mappingBesoins.put(besoin.getEmailFamille(), intervalsList);
        }
        return mappingBesoins;
    }
}
