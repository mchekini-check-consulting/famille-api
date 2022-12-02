package fr.checkconsulting.gardeenfant.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.checkconsulting.gardeenfant.exception.MapperException;
import fr.checkconsulting.gardeenfant.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ChatService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final ProducerService producerService;
    private final Environment env;

    public ChatService(ProducerService producerService, Environment env) {
        this.producerService = producerService;
        this.env = env;
    }

    public void sendMessage(Message message) {
        producerService.sendMessage(env.getProperty("producer.kafka.topic-name"), toJson(message));
    }
    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    public Set<String> getTopics() {
        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            Map<String, List<PartitionInfo>> map = consumer.listTopics();
            log.info("data : ", map.keySet());
            return map.keySet();
        }
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
