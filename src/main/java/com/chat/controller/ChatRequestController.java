package com.chat.controller;

import com.chat.dto.ChatRequestDto;
import com.chat.dto.CreateChatRequestDto;
import com.chat.service.ChatRequestService;
import com.chat.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat-request")
public class ChatRequestController {
    private final ChatRequestService chatRequestService;
    private final JwtUtil jwtUtil;

    public ChatRequestController(ChatRequestService chatRequestService, JwtUtil jwtUtil) {
        this.chatRequestService = chatRequestService;
        this.jwtUtil = jwtUtil;
    }

    private Long extractUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.extractUserId(token);
        }
        throw new RuntimeException("Unauthorized");
    }

    @PostMapping
    public ResponseEntity<ChatRequestDto> sendRequest(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody CreateChatRequestDto request) {
        Long userId = extractUserIdFromToken(token);
        ChatRequestDto response = chatRequestService.sendRequest(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<ChatRequestDto> acceptRequest(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = extractUserIdFromToken(token);
        ChatRequestDto response = chatRequestService.acceptRequest(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<ChatRequestDto> rejectRequest(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = extractUserIdFromToken(token);
        ChatRequestDto response = chatRequestService.rejectRequest(id, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ChatRequestDto>> getPendingRequests(
            @RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = extractUserIdFromToken(token);
        List<ChatRequestDto> requests = chatRequestService.getPendingRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/accepted")
    public ResponseEntity<List<ChatRequestDto>> getAcceptedRequests(
            @RequestHeader(value = "Authorization", required = false) String token) {
        Long userId = extractUserIdFromToken(token);
        List<ChatRequestDto> requests = chatRequestService.getAcceptedRequests(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRequestDto> getRequestById(@PathVariable Long id) {
        ChatRequestDto request = chatRequestService.getRequestById(id);
        return ResponseEntity.ok(request);
    }
}
