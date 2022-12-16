package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.entity.Famille;
import fr.checkconsulting.gardeenfant.entity.Intervention;
import fr.checkconsulting.gardeenfant.repository.FamilleRepository;
import fr.checkconsulting.gardeenfant.services.InterventionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/intervention")
public class InterventionResourceNounou {
    private static final Logger LOG = LoggerFactory.getLogger(InterventionService.class);
    private final InterventionService interventionService;
    private final FamilleRepository familleRepository;

   public InterventionResourceNounou(InterventionService interventionService, FamilleRepository familleRepository){
        this.interventionService = interventionService;
        this.familleRepository = familleRepository;
    }

    @GetMapping("/reject")
    public List<InfosInt> rejectInterventionNounou(@RequestParam("emailFamille") String emailFamille, @RequestParam("emailNounou") String emailNounou) {
        interventionService.rejectIntervention(emailFamille, emailNounou);
        return getInterventions(emailNounou);
    }

    @GetMapping("/confirm")
    public List<InfosInt> acceptInterventionNounou(@RequestParam("emailFamille") String emailFamille, @RequestParam("emailNounou") String emailNounou) {
        interventionService.acceptIntervention(emailFamille, emailNounou);
        return getInterventions(emailNounou);
    }

    @GetMapping("/get-all-interventions")
    public List<InfosInt> getAllInterventionsNounou(@RequestParam("emailNounou") String emailNounou) {
        return getInterventions(emailNounou);
    }

    private List<InfosInt> getInterventions(String emailNounou){
        Map<String, List<Intervention>> listIntervention = interventionService.getAllInterventionsByNounou(emailNounou);
        List<Famille> listFamille = familleRepository.findAll();

        List list = new ArrayList(listIntervention.keySet());

        List<InfosInt> data = new ArrayList();
        list.forEach(e -> data.add(getData(listIntervention.get(e), listFamille)));

        LOG.info("Data : {}", data.stream().collect(Collectors.toList()));

        return data.stream().collect(Collectors.toList());
    }

    public static InfosInt getData(List<Intervention> e, List<Famille> listFamilles){
        List<Infos> intInfos = new ArrayList();
        Infos obj = new Infos();

        Famille famille = listFamilles.stream().filter(n -> e.get(0).getEmailFamille().equals(n.getEmail())).findFirst().orElse(null);

        e.forEach(d -> {
           obj.setJour(d.getJour());
           obj.setMatin(d.getMatin());
           obj.setMidi(d.getMidi());
           obj.setSoir(d.getSoir());
           intInfos.add(obj);
        });

        InfosInt response = new InfosInt();
        response.setNom(famille == null ? "" : famille.getNom().toUpperCase());
        response.setPrenom(famille == null ? "" : famille.getPrenomRepresentant());
        response.setEmailNounou(e.get(0).getEmailFamille());
        response.setPeriode(e.get(0).getDebutIntervention().toString().substring(0,10) + " au " + e.get(0).getFinIntervention().toString().substring(0,10));
        response.setEtat(e.get(0).getEtat());
        response.setDateIntervention(e.get(0).getTimeIntervention().toString().substring(0,10));
        response.setDetailIntervention(intInfos);

        return response;
    }
}
