package com.chat.service;

import com.chat.dto.MessageDto;
import com.chat.dto.WebSocketMessageDto;
import com.chat.model.ChatRequest;
import com.chat.model.Message;
import com.chat.model.User;
import com.chat.repository.ChatRequestRepository;
import com.chat.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRequestRepository chatRequestRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, ChatRequestRepository chatRequestRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.chatRequestRepository = chatRequestRepository;
        this.userService = userService;
    }

    public MessageDto saveMessage(WebSocketMessageDto messageDto) {
        User sender = userService.getUserEntityById(messageDto.getSenderId());
        User receiver = userService.getUserEntityById(messageDto.getReceiverId());

        // Check if chat is allowed (request must be accepted)
        ChatRequest chatRequest = chatRequestRepository
                .findExistingRequest(sender, receiver)
                .orElseThrow(() -> new RuntimeException("Chat request not found"));

        if (!chatRequest.getStatus().equals(ChatRequest.RequestStatus.ACCEPTED)) {
            throw new RuntimeException("Chat request must be accepted to send messages");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDto.getContent());
        message.setFileUrl(messageDto.getFileUrl());
        message.setType(Message.MessageType.valueOf(messageDto.getType().toUpperCase()));

        Message saved = messageRepository.save(message);
        return MessageDto.fromEntity(saved);
    }

    public List<MessageDto> getChatHistory(Long userId, Long otherUserId) {
        User user = userService.getUserEntityById(userId);
        User other = userService.getUserEntityById(otherUserId);

        // Check if chat is allowed
        ChatRequest chatRequest = chatRequestRepository
                .findExistingRequest(user, other)
                .orElseThrow(() -> new RuntimeException("Chat request not found"));

        if (!chatRequest.getStatus().equals(ChatRequest.RequestStatus.ACCEPTED)) {
            throw new RuntimeException("Chat request must be accepted");
        }

        return messageRepository.findChatHistory(user, other)
                .stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }
}
