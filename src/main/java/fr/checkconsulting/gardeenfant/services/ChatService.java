package fr.checkconsulting.gardeenfant.services;

import fr.checkconsulting.gardeenfant.dto.MessageDTO;
import fr.checkconsulting.gardeenfant.entity.Message;
import fr.checkconsulting.gardeenfant.repository.ChatRepository;
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
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;
    private final Logger LOG = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    public ChatService(Environment env, KafkaTemplate kafkaTemplate, ChatRepository chatRepository, ModelMapper modelMapper) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
        this.chatRepository = chatRepository;
        this.modelMapper = modelMapper;
    }

    public List<Message> getMessages() throws  Exception {
        String email = CommonData.getEmail();
        List<Message> result = chatRepository
                .getAllByEmailSourceOrEmailDest(email, email)
                .stream()
                .map(message -> modelMapper.map(message, Message.class))
                .collect(Collectors.toList());

        LOG.info("Result : {}", result);
        return result;
    }

    public Number getUnreadMessages() throws  Exception {
        String emailDest = CommonData.getEmail();
        Number result = chatRepository
                .getUnreadMessages(emailDest);

        LOG.info("Result all unread messages : {}", result);
        return result;
    }

    public List[] getUnreadMessagesByNounou() throws  Exception {
        String emailDest = CommonData.getEmail();
        List[] result = chatRepository
                .getUnreadMessagesByNounou(emailDest);

        LOG.info("Result unread message by nounou : {}", result);
        return result;
    }

    public void sendMessage(Message message) {
        LOG.info("Sending User Json Serializer : {}", message);
        kafkaTemplate.send(env.getProperty("producer.kafka.topic-name"), message);
    }

    public int setMessageRead(MessageDTO data){
        LOG.info("Data set Message Read : {} : {}", data.emailSource, data.emailDest);
        return chatRepository.setMessageRead(data.emailSource, data.emailDest);
    }
}
