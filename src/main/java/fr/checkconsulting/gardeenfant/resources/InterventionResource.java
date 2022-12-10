package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.entity.Intervention;
import fr.checkconsulting.gardeenfant.entity.InterventionsDTO;
import fr.checkconsulting.gardeenfant.security.CommonData;
import fr.checkconsulting.gardeenfant.services.ChatService;
import fr.checkconsulting.gardeenfant.services.InterventionService;
import fr.checkconsulting.gardeenfant.services.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/famille/intervention")
public class InterventionResource {
    private final Logger LOG = LoggerFactory.getLogger(ChatService.class);
    private final InterventionService interventionService;

    @Value(value="${producer.kafka.topic-name-intervention}")
    private String topicname;

    private final ProducerService producerService;

    public InterventionResource(InterventionService interventionService, ProducerService producerService){
        this.interventionService = interventionService;
        this.producerService = producerService;
    }

    @PostMapping("/create")
    @Transactional
    public void sendIntervention(@RequestBody Intervention[] interventions) throws Exception {
        for (Intervention intervention: interventions) {
            intervention.setTimeIntervention(LocalDateTime.now());
            intervention.setEmailFamille(CommonData.getEmail());

            LOG.info("Sending User Json Serializer : {}", intervention);
            producerService.sendIntervention(topicname, intervention);
        }
    }

    @GetMapping("/get-all-interventions")
    public List<InterventionsDTO> getDispoAllNounou() {
        return interventionService.getDispoAllInterventions();
    }
}
