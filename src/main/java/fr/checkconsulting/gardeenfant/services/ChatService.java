package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.entity.Message;
import fr.checkconsulting.gardeenfant.repository.MessageRepository;
import fr.checkconsulting.gardeenfant.security.CommonData;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final Environment env;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final Logger LOG = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public ChatService(Environment env, KafkaTemplate kafkaTemplate, MessageRepository messageRepository, ModelMapper modelMapper) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    public List<Message> getMessages() throws  Exception {
        String email = CommonData.getEmail();
        List<Message> result = messageRepository
                .getAllByEmailSourceOrEmailDest(email, email)
                .stream()
                .map(message -> modelMapper.map(message, Message.class))
                .collect(Collectors.toList());

        LOG.info("Result : {}", result);
        if(!result.isEmpty()) {
            return result;
        }else {
            throw new Exception("La requête a échouée");
        }
    }

    public void sendMessage(Message message) {
        LOG.info("Sending User Json Serializer : {}",message);
        kafkaTemplate.send(env.getProperty("producer.kafka.topic-name"), message);
    }
}
