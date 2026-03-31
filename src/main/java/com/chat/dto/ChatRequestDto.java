package com.chat.dto;

import com.chat.model.ChatRequest;

public class ChatRequestDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private ChatRequest.RequestStatus status;

    public ChatRequestDto() {
    }

    public ChatRequestDto(Long id, Long senderId, String senderName, Long receiverId, String receiverName, ChatRequest.RequestStatus status) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public ChatRequest.RequestStatus getStatus() {
        return status;
    }

    public void setStatus(ChatRequest.RequestStatus status) {
        this.status = status;
    }

    public static ChatRequestDto fromEntity(ChatRequest request) {
        return new ChatRequestDto(
                request.getId(),
                request.getSender().getId(),
                request.getSender().getUsername(),
                request.getReceiver().getId(),
                request.getReceiver().getUsername(),
                request.getStatus()
        );
    }
}
