package com.example.entity;

import java.time.LocalDateTime;

import com.example.enums.ChatChannel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatlogs")
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatid;

    @ManyToOne
    @JoinColumn(name = "senderid")
    private Player sender;

    @Enumerated(EnumType.STRING)
    private ChatChannel channel;

    @ManyToOne
    @JoinColumn(name = "receiverid", nullable = true)
    private Player receiver;

    @ManyToOne
    @JoinColumn(name = "mapid", nullable = true)
    private Maps map;

    @Column(columnDefinition = "TEXT")
    private String message;
    
    private LocalDateTime sentat;

	public Integer getChatid() {
		return chatid;
	}

	public void setChatid(Integer chatid) {
		this.chatid = chatid;
	}

	public Player getSender() {
		return sender;
	}

	public void setSender(Player sender) {
		this.sender = sender;
	}

	public ChatChannel getChannel() {
		return channel;
	}

	public void setChannel(ChatChannel channel) {
		this.channel = channel;
	}

	public Player getReceiver() {
		return receiver;
	}

	public void setReceiver(Player receiver) {
		this.receiver = receiver;
	}

	public Maps getMap() {
		return map;
	}

	public void setMap(Maps map) {
		this.map = map;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSentat() {
		return sentat;
	}

	public void setSentat(LocalDateTime sentat) {
		this.sentat = sentat;
	}
    
    
}
