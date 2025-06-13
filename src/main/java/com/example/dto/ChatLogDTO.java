package com.example.dto;

import java.time.LocalDateTime;

import com.example.entity.ChatLog;

public class ChatLogDTO {

	private int id;
    private int senderId;
    private String senderName;
    private Integer receiverId;     
    private String receiverName;
    private Integer mapId;          
    private String message;
    private LocalDateTime sentAt;
    
	public ChatLogDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Integer getMapId() {
		return mapId;
	}

	public void setMapId(Integer mapId) {
		this.mapId = mapId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
	
	public static ChatLogDTO fromEntity(ChatLog chatLog) {
	    ChatLogDTO dto = new ChatLogDTO();
	    dto.setId(chatLog.getChatid());
	    dto.setSenderId(chatLog.getSender().getPlayerid());
	    dto.setSenderName(chatLog.getSender().getName());

	    if (chatLog.getReceiver() != null) {
	        dto.setReceiverId(chatLog.getReceiver().getPlayerid());
	        dto.setReceiverName(chatLog.getReceiver().getName());
	    }

	    if (chatLog.getMap() != null) {
	        dto.setMapId(chatLog.getMap().getId());
	    }

	    dto.setMessage(chatLog.getMessage());
	    dto.setSentAt(chatLog.getSentat());

	    return dto;
	}
    
}
