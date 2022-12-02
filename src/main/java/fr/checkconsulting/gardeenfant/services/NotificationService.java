package fr.checkconsulting.gardeenfant.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.checkconsulting.gardeenfant.exception.MapperException;
import fr.checkconsulting.gardeenfant.model.Notification;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final ProducerService producerService;
    private final Environment env;

    public NotificationService(ProducerService producerService, Environment env) {
        this.producerService = producerService;
        this.env = env;
    }

    public void send(Notification notification) {
        producerService.sendMessage(env.getProperty("producer.kafka.topic-name"), toJson(notification));
    }


    /**
     * Convert Object to json
     *
     * @param object object
     * @return string json
     */
    private <T> String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MapperException(e.getMessage());
        }
    }
}