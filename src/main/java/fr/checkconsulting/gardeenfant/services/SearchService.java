package fr.checkconsulting.gardeenfant.services;

import com.google.common.collect.Iterables;
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

    public List<NounouDto> getNounouByCriteria(String nom, String prenom, String ville) {

        String url = nounouUrl + "/api/v1/search/nounou?nom=" + nom + "&prenom=" + prenom + "&ville=" + ville;
        ResponseEntity<NounouDto[]> nounouDtos = restTemplate.getForEntity(url, NounouDto[].class);

        return Arrays.stream(nounouDtos.getBody()).collect(Collectors.toList());
    }

    public List<FamilleDTO> getFamilleByCriteria(String nom, String prenom, String ville, int jour, String heureDebut, String heureFin) {

        if ("".equals(nom)) nom = null;
        if ("".equals(prenom)) prenom = null;
        if ("".equals(ville)) ville = null;
        if ("".equals(heureDebut)) heureDebut = null;
        if ("".equals(heureFin)) heureFin = null;

        List<Famille> familles = familleRepository.getFamillesByCriteria(nom, prenom, ville);
        List<Besoins> besoins = besoinsRepository.findAllByJour(jour);
        if (besoins.isEmpty()) {
            Iterables.removeAll(familles, familles);
        }
        else {
            Map<String, List<LocalTime>> mappedBesoins = mapBesoins(besoins);
            if (heureDebut != null && heureFin != null) {
                List<String> emails = filterBesoinsByTimeInterval(mappedBesoins, LocalTime.parse(heureDebut), LocalTime.parse(heureFin));
                familles.removeIf(famille -> !emails.contains(famille.getEmail()));
            } else {
                familles.removeIf(famille -> besoins.stream().noneMatch(besoin -> besoin.getEmailFamille().equals(famille.getEmail())));
            }
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

    private List<String> filterBesoinsByTimeInterval(Map<String, List<LocalTime>> besoins, LocalTime heureDebut, LocalTime heureFin) {
        List<String> emails = new ArrayList<>();
        for (Map.Entry<String, List<LocalTime>> besoin : besoins.entrySet()) {
            boolean validTimeInterval = false;
            List<LocalTime> intervals = besoin.getValue();
            for (int i = 0; i < intervals.size(); i += 2) {
                if (!intervals.get(i).isAfter(heureDebut) && !heureFin.isAfter(intervals.get(i + 1))) {
                    validTimeInterval = true;
                    break;
                }
            }
            if (validTimeInterval) {
                emails.add(besoin.getKey());
            }
        }
        return emails;
    }

    private HashMap<String, List<LocalTime>> mapBesoins(List<Besoins> besoins) {
        HashMap<String, List<LocalTime>> transformedBesoins = new HashMap<>();
        for (Besoins besoin : besoins) {
            List<LocalTime> timeList = new ArrayList<>();
            if (besoin.getBesoin_matin_debut() != null) timeList.add(besoin.getBesoin_matin_debut());
            if (besoin.getBesoin_matin_fin() != null) timeList.add(besoin.getBesoin_matin_fin());
            if (besoin.getBesoin_midi_debut() != null) timeList.add(besoin.getBesoin_midi_debut());
            if (besoin.getBesoin_midi_fin() != null) timeList.add(besoin.getBesoin_midi_fin());
            if (besoin.getBesoin_soir_debut() != null) timeList.add(besoin.getBesoin_soir_debut());
            if (besoin.getBesoin_soir_fin() != null) timeList.add(besoin.getBesoin_soir_fin());
            if (timeList.size() == 6 && timeList.get(1).equals(timeList.get(2))) {
                timeList.remove(1);
                timeList.remove(1);
            }
            if (timeList.size() == 4 && timeList.get(1).equals(timeList.get(2))) {
                timeList.remove(1);
                timeList.remove(1);
            }
            transformedBesoins.put(besoin.getEmailFamille(), timeList);
        }
        return transformedBesoins;
    }

    private Besoins buildDisponibiliteCriteria(String jour, String heureDebut, String heureFin) {
        Besoins besoins = new Besoins();
        if ("".equals(jour)) {
            besoins.setJour(-1);
        } else {
            try {
                besoins.setJour(Integer.parseInt(jour));
            } catch (NumberFormatException e) {
                besoins.setJour(-1);
            }
        }

        if (!"".equals(heureDebut) && !"".equals(heureFin)) {
            LocalTime parsedHeureDebut = LocalTime.parse(heureDebut);
            LocalTime parsedHeureFin = LocalTime.parse(heureFin);
            LocalTime matin = LocalTime.parse("07:00:00");
            LocalTime midi = LocalTime.parse("12:00:00");
            LocalTime aprem = LocalTime.parse("16:00:00");
            LocalTime soir = LocalTime.parse("21:00:00");
            LocalTime minuit = LocalTime.parse("00:00:00");

            if (parsedHeureDebut.isBefore(matin)
                    || parsedHeureDebut.isAfter(soir)
                    || parsedHeureFin.isBefore(matin)
                    || parsedHeureFin.isAfter(soir)
            ) {
                besoins.setBesoin_matin_debut(minuit);
                besoins.setBesoin_matin_fin(minuit);
                besoins.setBesoin_midi_debut(minuit);
                besoins.setBesoin_midi_fin(minuit);
                besoins.setBesoin_soir_debut(minuit);
                besoins.setBesoin_soir_fin(minuit);
            }
            if (!matin.isAfter(parsedHeureDebut) && parsedHeureDebut.isBefore(midi)) {
                besoins.setBesoin_matin_debut(parsedHeureDebut);
                if (!parsedHeureFin.isAfter(midi)) {
                    besoins.setBesoin_matin_fin(parsedHeureFin);
                }
                if (midi.isBefore(parsedHeureFin) && !parsedHeureFin.isAfter(aprem)) {
                    besoins.setBesoin_matin_fin(midi);
                    besoins.setBesoin_midi_debut(midi);
                    besoins.setBesoin_midi_fin(parsedHeureFin);
                }
                if (aprem.isBefore(parsedHeureFin) && !parsedHeureFin.isAfter(soir)) {
                    besoins.setBesoin_matin_fin(midi);
                    besoins.setBesoin_midi_debut(midi);
                    besoins.setBesoin_midi_fin(aprem);
                    besoins.setBesoin_soir_debut(aprem);
                    besoins.setBesoin_soir_fin(parsedHeureFin);
                }
            }
            if (!midi.isAfter(parsedHeureDebut) && parsedHeureDebut.isBefore(aprem)) {
                besoins.setBesoin_midi_debut(parsedHeureDebut);
                if (!parsedHeureFin.isAfter(aprem)) {
                    besoins.setBesoin_midi_fin(parsedHeureFin);
                }
                if (aprem.isBefore(parsedHeureFin) && !parsedHeureFin.isAfter(soir)) {
                    besoins.setBesoin_midi_fin(aprem);
                    besoins.setBesoin_soir_debut(aprem);
                    besoins.setBesoin_soir_fin(parsedHeureFin);
                }
            }
            if (!aprem.isAfter(parsedHeureDebut) && parsedHeureDebut.isBefore(soir)) {
                besoins.setBesoin_soir_debut(parsedHeureDebut);
                if (!parsedHeureFin.isAfter(soir)) {
                    besoins.setBesoin_soir_fin(parsedHeureFin);
                }
            }
        }

        return besoins;
    }
}
