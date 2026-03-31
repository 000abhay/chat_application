package com.chat.dto;

public class WebSocketMessageDto {
    private Long senderId;
    private Long receiverId;
    private String content;
    private String fileUrl;
    private String type;

    public WebSocketMessageDto() {
    }

    public WebSocketMessageDto(Long senderId, Long receiverId, String content, String fileUrl, String type) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.fileUrl = fileUrl;
        this.type = type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
