package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.model.Message;
import fr.checkconsulting.gardeenfant.services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("api/v1/famille/chat")
public class ChatResource {
    private final ChatService chatService;

    public ChatResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/get")
    public ResponseEntity<Void> getChat() {
        chatService.getTopics();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) throws Exception {
        message.setTime(LocalDateTime.now());
        chatService.sendMessage(message);
    }
}
