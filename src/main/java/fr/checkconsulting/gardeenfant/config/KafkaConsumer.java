package fr.checkconsulting.gardeenfant.config;

import fr.checkconsulting.gardeenfant.entity.Message;
import fr.checkconsulting.gardeenfant.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    private final ChatRepository chatRepository;

    @Autowired
    public KafkaConsumer(ChatRepository chatRepository) {this.chatRepository = chatRepository;}

    @KafkaListener(topics = "chat-famille", groupId = "famille-group-id")
    public void listenSenderMessage(Message data) {
        System.out.println("Message received by consumer : " + data);
        // Sauvegarder une copie sur la base de données famille
        chatRepository.save(data);
    }
}
