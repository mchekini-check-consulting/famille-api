package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Intervention;
import fr.checkconsulting.gardeenfant.repository.InterventionRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class InterventionService {
    private final Environment env;
    private final ModelMapper modelMapper;
    private final Logger LOG = LoggerFactory.getLogger(ChatService.class);
    private final InterventionRepository interventionRepository;

    @Value("${nounou.application.url}")
    String nounouUrl;

    @Autowired
    private KafkaTemplate<String, Intervention> kafkaTemplate;
    private final RestTemplate restTemplate;

    public InterventionService(Environment env, KafkaTemplate kafkaTemplate, ModelMapper modelMapper, RestTemplate restTemplate, InterventionRepository interventionRepository) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
        this.interventionRepository = interventionRepository;
    }

    public void sendIntervention(Intervention intervention) {
        LOG.info("Sending User Json Serializer : {}",intervention);
        kafkaTemplate.send(env.getProperty("producer.kafka.topic-name-intervention"), intervention);
    }

    public Map<String, List<Intervention>> getAllInterventions() {
        return interventionRepository.findAll()
                .parallelStream()
                .map(record -> new Intervention(record.getTimeIntervention(), record.getDebutIntervention(), record.getFinIntervention(), record.getJour(), record.getMatin(), record.getMidi(), record.getSoir(), record.getEmailFamille(), record.getEmailNounou(), record.getEtat()))
                .collect(groupingBy(Intervention::getEmailNounou));
    }
}
