package fr.checkconsulting.gardeenfant.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.checkconsulting.gardeenfant.entity.Intervention;
import fr.checkconsulting.gardeenfant.entity.Message;
import fr.checkconsulting.gardeenfant.repository.ChatRepository;
import fr.checkconsulting.gardeenfant.repository.InterventionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    private final ChatRepository chatRepository;
    private final InterventionRepository interventionRepository;

    @Autowired
    public KafkaConsumer(ChatRepository chatRepository, InterventionRepository interventionRepository) {
        this.chatRepository = chatRepository;
        this.interventionRepository = interventionRepository;
    }

    @KafkaListener(topics = "chat-famille", groupId = "famille-group-id")
    public void listenSenderMessage(Message data) {
        log.info("Message received by consumer {}", data);
        // Sauvegarder une copie sur la base de données famille
        chatRepository.save(data);
    }

    @KafkaListener(topics = "intervention-app", groupId = "intervention-group-id")
    public void listenSenderIntervention(ConsumerRecord data) throws JsonProcessingException {

        Intervention intervention = new ObjectMapper().readValue(data.value().toString(), Intervention.class);

        log.info("Message received by consumer {}", data.value());
        // Sauvegarder une copie sur la base de données famille
        interventionRepository.save(intervention);
    }
}
