package com.chat.dto;

public class CreateChatRequestDto {
    private Long receiverId;

    public CreateChatRequestDto() {
    }

    public CreateChatRequestDto(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
