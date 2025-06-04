package com.example.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.dto.ChatMessage;
import com.example.entity.ChatLog;
import com.example.entity.Player;
import com.example.entity.PlayerPosition;
import com.example.service.ChatLogService;
import com.example.service.PlayerPositionService;
import com.example.service.PlayerService;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private final ChatLogService chatLogService;
    private final PlayerService playerService;
    private final PlayerPositionService playerPositionService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
 
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

	public ChatWebSocketHandler(ChatLogService chatLogService, PlayerService playerService, PlayerPositionService playerPositionService) {
		this.chatLogService = chatLogService;
		this.playerService = playerService;
		this.playerPositionService = playerPositionService;
	}
    
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer playerId = getPlayerIdFromSession(session);
        if (playerId != null) {
            sessions.put(playerId, session);
            System.out.println("Player " + playerId + " connected via WebSocket.");
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }
	
	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Integer playerId = getPlayerIdFromSession(session);
        if (playerId != null) {
            sessions.remove(playerId);
            System.out.println("Player " + playerId + " disconnected.");
        }
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage incoming = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        Integer senderId = getPlayerIdFromSession(session);
        if (senderId == null) return;

        Player sender = playerService.findByPlayerId(senderId).orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + senderId));
        PlayerPosition playerPosition = playerPositionService.getPositionByPlayerId(senderId).orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + senderId));
        ChatLog chat = new ChatLog();
        chat.setSender(sender);
        chat.setMessage(incoming.getMessage());
        chat.setChannel(incoming.getChannel());
        chat.setSentat(LocalDateTime.now());

        switch (incoming.getChannel()) {
            case MAP -> {
                chat.setMap(playerPosition.getMap());
                chatLogService.sendMessage(chat);
                broadcastToMap(playerPosition.getMap().getMapid(), chat);
            }
            case PRIVATE -> {
                Player receiver = playerService.findByPlayerId(incoming.getReceiverId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + incoming.getReceiverId()));
                chat.setReceiver(receiver);
                chatLogService.sendMessage(chat);
                sendToPrivateUsers(senderId, incoming.getReceiverId(), chat);
            }
            case GLOBAL -> {
                chatLogService.sendMessage(chat);
                broadcastToAll(chat);
            }
        }
    }
	
	private Integer getPlayerIdFromSession(WebSocketSession session) {
		String query = session.getUri().getQuery();
	    if (query != null) {
	        for (String param : query.split("&")) {
	            String[] pair = param.split("=");
	            if (pair.length == 2 && pair[0].equals("playerId")) {
	                try {
	                    return Integer.parseInt(pair[1]);
	                } catch (NumberFormatException e) {
	                    return null;
	                }
	            }
	        }
	    }
	    return null;
    }
	
	private void broadcastToMap(int mapId, ChatLog chat) throws Exception {
        for (Map.Entry<Integer, WebSocketSession> entry : sessions.entrySet()) {
            PlayerPosition playerPosition = playerPositionService.getPositionByPlayerId(entry.getKey()).orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + entry.getKey()));
            if (playerPosition.getMap() != null && playerPosition.getMap().getMapid() == mapId) {
                send(entry.getValue(), chat);
            }
        }
    }
	
	private void broadcastToAll(ChatLog chat) throws Exception {
        for (WebSocketSession session : sessions.values()) {
            send(session, chat);
        }
    }
	
	private void send(WebSocketSession session, ChatLog chat) throws Exception {
        if (session != null && session.isOpen()) {
            ChatMessage outgoing = ChatMessage.fromEntity(chat);
            String json = objectMapper.writeValueAsString(outgoing);
            session.sendMessage(new TextMessage(json));
        }
    }
	
	private void sendToPrivateUsers(int senderId, int receiverId, ChatLog chat) throws Exception {
        send(sessions.get(senderId), chat);
        if (sessions.containsKey(receiverId)) {
            send(sessions.get(receiverId), chat);
        }
    }
}
