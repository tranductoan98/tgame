package com.example.dto;

import java.time.LocalDateTime;

import com.example.entity.ChatLog;
import com.example.enums.ChatChannel;

public class ChatMessage {
	private String message;
    private ChatChannel channel;
    private Integer receiverId;
    private String senderName;
    private LocalDateTime sentat;
    
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ChatChannel getChannel() {
		return channel;
	}

	public void setChannel(ChatChannel channel) {
		this.channel = channel;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public LocalDateTime getSentat() {
		return sentat;
	}

	public void setSentat(LocalDateTime sentat) {
		this.sentat = sentat;
	}

	public static ChatMessage fromEntity(ChatLog log) {
        ChatMessage dto = new ChatMessage();
        dto.setMessage(log.getMessage());
        dto.setChannel(log.getChannel());
        dto.setSenderName(log.getSender().getName());
        dto.setSentat(log.getSentat());
        return dto;
    }
}
