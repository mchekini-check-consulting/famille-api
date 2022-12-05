package fr.checkconsulting.gardeenfant.resources;

import fr.checkconsulting.gardeenfant.entity.Message;
import fr.checkconsulting.gardeenfant.services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/famille/chat")
public class ChatResource {
    private final ChatService chatService;

    public ChatResource(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Message>> getMessages() throws Exception {
        return ResponseEntity.ok(chatService.getMessages());
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody Message message) throws Exception {
        message.setTimeMessage(LocalDateTime.now());
        chatService.sendMessage(message);
    }
}
