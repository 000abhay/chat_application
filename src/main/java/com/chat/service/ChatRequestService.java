package com.chat.service;

import com.chat.dto.ChatRequestDto;
import com.chat.dto.CreateChatRequestDto;
import com.chat.model.ChatRequest;
import com.chat.model.User;
import com.chat.repository.ChatRequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRequestService {
    private final ChatRequestRepository chatRequestRepository;
    private final UserService userService;

    public ChatRequestService(ChatRequestRepository chatRequestRepository, UserService userService) {
        this.chatRequestRepository = chatRequestRepository;
        this.userService = userService;
    }

    public ChatRequestDto sendRequest(Long senderId, CreateChatRequestDto request) {
        User sender = userService.getUserEntityById(senderId);
        User receiver = userService.getUserEntityById(request.getReceiverId());

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Cannot send chat request to yourself");
        }

        chatRequestRepository.findExistingRequest(sender, receiver)
                .ifPresent(existing -> {
                    throw new RuntimeException("Request already exists with this user");
                });

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setSender(sender);
        chatRequest.setReceiver(receiver);
        chatRequest.setStatus(ChatRequest.RequestStatus.PENDING);

        ChatRequest saved = chatRequestRepository.save(chatRequest);
        return ChatRequestDto.fromEntity(saved);
    }

    public ChatRequestDto acceptRequest(Long requestId, Long userId) {
        ChatRequest chatRequest = chatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!chatRequest.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Only receiver can accept this request");
        }

        chatRequest.setStatus(ChatRequest.RequestStatus.ACCEPTED);
        ChatRequest saved = chatRequestRepository.save(chatRequest);
        return ChatRequestDto.fromEntity(saved);
    }

    public ChatRequestDto rejectRequest(Long requestId, Long userId) {
        ChatRequest chatRequest = chatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!chatRequest.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Only receiver can reject this request");
        }

        chatRequest.setStatus(ChatRequest.RequestStatus.REJECTED);
        ChatRequest saved = chatRequestRepository.save(chatRequest);
        return ChatRequestDto.fromEntity(saved);
    }

    public List<ChatRequestDto> getPendingRequests(Long userId) {
        User user = userService.getUserEntityById(userId);
        return chatRequestRepository.findByReceiverAndStatus(user, ChatRequest.RequestStatus.PENDING)
                .stream()
                .map(ChatRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatRequestDto> getAcceptedRequests(Long userId) {
        User user = userService.getUserEntityById(userId);
        List<ChatRequest> sent = chatRequestRepository.findBySenderAndStatus(user, ChatRequest.RequestStatus.ACCEPTED);
        List<ChatRequest> received = chatRequestRepository.findByReceiverAndStatus(user, ChatRequest.RequestStatus.ACCEPTED);

        sent.addAll(received);
        return sent.stream()
                .map(ChatRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    public ChatRequestDto getRequestById(Long requestId) {
        ChatRequest request = chatRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return ChatRequestDto.fromEntity(request);
    }
}
