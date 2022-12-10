package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Intervention;
import fr.checkconsulting.gardeenfant.entity.InterventionsDTO;
import fr.checkconsulting.gardeenfant.security.CommonData;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterventionService {
    private final Environment env;
    private final ModelMapper modelMapper;
    private final Logger LOG = LoggerFactory.getLogger(ChatService.class);

    @Value("${nounou.application.url}")
    String nounouUrl;

    @Autowired
    private KafkaTemplate<String, Intervention> kafkaTemplate;
    private final RestTemplate restTemplate;

    public InterventionService(Environment env, KafkaTemplate kafkaTemplate, ModelMapper modelMapper, RestTemplate restTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void sendIntervention(Intervention[] interventions) {
        for (Intervention intervention: interventions) {
            intervention.setTimeIntervention(LocalDateTime.now());
            intervention.setEmailFamille(CommonData.getEmail());

            LOG.info("Sending User Json Serializer : {}",intervention);
            kafkaTemplate.send(env.getProperty("producer.kafka.topic-name-intervention"), intervention);
        }
    }

    public List<InterventionsDTO> getDispoAllInterventions() {
        return null;
    }
}
