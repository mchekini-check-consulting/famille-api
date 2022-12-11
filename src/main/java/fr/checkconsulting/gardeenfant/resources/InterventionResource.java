package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.dto.NounouDto;
import fr.checkconsulting.gardeenfant.entity.Intervention;
import fr.checkconsulting.gardeenfant.security.CommonData;
import fr.checkconsulting.gardeenfant.services.ChatService;
import fr.checkconsulting.gardeenfant.services.InterventionService;
import fr.checkconsulting.gardeenfant.services.ProducerService;
import fr.checkconsulting.gardeenfant.services.SearchService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
class Infos {
    public String jour;
    public String matin;
    public String midi;
    public String soir;
}

@Data
class InfosInt {
    public String nom;
    public String prenom;
    public String emailNounou;
    public String periode;
    public String etat;
    public String dateIntervention;
    List<Infos> detailIntervention;
}

@RestController
@RequestMapping("api/v1/famille/intervention")
public class InterventionResource {
    private static final Logger LOG = LoggerFactory.getLogger(ChatService.class);
    private final InterventionService interventionService;
    private final SearchService searchService;

    @Value(value="${producer.kafka.topic-name-intervention}")
    private String topicname;

    private final ProducerService producerService;

    public InterventionResource(InterventionService interventionService, ProducerService producerService, SearchService searchService){
        this.interventionService = interventionService;
        this.producerService = producerService;
        this.searchService = searchService;
    }

    @PostMapping("/create")
    @Transactional
    public void sendIntervention(@RequestBody Intervention[] interventions) throws Exception {
        for (Intervention intervention: interventions) {
            intervention.setTimeIntervention(LocalDateTime.now());
            intervention.setEmailFamille(CommonData.getEmail());

            interventionService.sendIntervention(intervention);
        }
    }

    @GetMapping("/get-all-interventions")
    public List<InfosInt> getAllInterventions() {
        Map<String, List<Intervention>> listIntervention = interventionService.getAllInterventions();
        List<NounouDto> listNounous = searchService.getNounouByCriteria("","","");

        List list = new ArrayList(listIntervention.keySet());

        List<InfosInt> data = new ArrayList();
        list.forEach(e -> data.add(getData(listIntervention.get(e), listNounous)));

        LOG.info("Data : {}", data.stream().collect(Collectors.toList()));

        return data.stream().collect(Collectors.toList());
    }

    public static InfosInt getData(List<Intervention> e, List<NounouDto> listNounous){
        List<Infos> intInfos = new ArrayList();
        Infos obj = new Infos();

        NounouDto nounou = listNounous.stream().filter(n -> e.get(0).getEmailNounou().equals(n.getMail())).findFirst().orElse(null);

        e.forEach(d -> {
           obj.setJour(d.getJour());
           obj.setMatin(d.getMatin());
           obj.setMidi(d.getMidi());
           obj.setSoir(d.getSoir());
           intInfos.add(obj);
        });

        InfosInt response = new InfosInt();
        response.setNom(nounou.getNom().toUpperCase());
        response.setPrenom(nounou.getPrenom());
        response.setEmailNounou(e.get(0).getEmailNounou());
        response.setPeriode(e.get(0).getDebutIntervention().toString().substring(0,10) + " au " + e.get(0).getFinIntervention().toString().substring(0,10));
        response.setEtat(e.get(0).getEtat());
        response.setDateIntervention(e.get(0).getTimeIntervention().toString().substring(0,10));
        response.setDetailIntervention(intInfos);

        return response;
    }
}
