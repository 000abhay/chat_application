package com.chat.controller;

import com.chat.dto.MessageDto;
import com.chat.dto.WebSocketMessageDto;
import com.chat.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class WebSocketController {
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(MessageService messageService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void handleMessage(WebSocketMessageDto messageDto) {
        try {
            // Save message to database
            MessageDto savedMessage = messageService.saveMessage(messageDto);

            // Send to receiver
            messagingTemplate.convertAndSendToUser(
                    messageDto.getReceiverId().toString(),
                    "/queue/messages",
                    savedMessage
            );

            // Send confirmation to sender
            messagingTemplate.convertAndSendToUser(
                    messageDto.getSenderId().toString(),
                    "/queue/messages",
                    savedMessage
            );
        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(
                    messageDto.getSenderId().toString(),
                    "/queue/errors",
                    "Error: " + e.getMessage()
            );
        }
    }

    @MessageMapping("/typing")
    public void handleTypingIndicator(WebSocketMessageDto messageDto) {
        messagingTemplate.convertAndSendToUser(
                messageDto.getReceiverId().toString(),
                "/queue/typing",
                messageDto.getSenderId() + " is typing..."
        );
    }
}

@RestController
@RequestMapping("/api/messages")
class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/history/{userId}/{otherUserId}")
    public List<MessageDto> getChatHistory(
            @PathVariable Long userId,
            @PathVariable Long otherUserId) {
        return messageService.getChatHistory(userId, otherUserId);
    }
}
