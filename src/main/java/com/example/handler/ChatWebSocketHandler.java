package com.example.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.dto.ChatMessage;
import com.example.dto.PlayerPositionDTO;
import com.example.entity.ChatLog;
import com.example.entity.Maps;
import com.example.entity.Player;
import com.example.service.ChatLogService;
import com.example.service.MapService;
import com.example.service.PlayerPositionService;
import com.example.service.PlayerService;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private final ChatLogService chatLogService;
    private final PlayerService playerService;
    private final PlayerPositionService playerPositionService;
    private final MapService mapService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
 
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

	public ChatWebSocketHandler(ChatLogService chatLogService, PlayerService playerService, PlayerPositionService playerPositionService, MapService mapService) {
		this.chatLogService = chatLogService;
		this.playerService = playerService;
		this.playerPositionService = playerPositionService;
		this.mapService = mapService;
	}
    
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer playerId = getPlayerIdFromSession(session);
        if (playerId != null) {
            sessions.put(playerId, session);
            System.out.println("Player " + playerId + " kết nối.");
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }
	
	@Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Integer playerId = getPlayerIdFromSession(session);
        if (playerId != null) {
            sessions.remove(playerId);
            System.out.println("Player " + playerId + " ngắt kết nối.");
        }
    }
	
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage incoming = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        Integer senderId = getPlayerIdFromSession(session);
        if (senderId == null) return;

        Player sender = playerService.findByPlayerId(senderId)
        		.orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + senderId));
        PlayerPositionDTO playerPosition = playerPositionService.getPositionByPlayerId(senderId)
        		.orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + senderId));
        Maps maps = mapService.getMapById(playerPosition.getMapId())
        		.orElseThrow(() -> new RuntimeException("Không tìm thấy Map với ID: " + playerPosition.getMapId()));
        
        ChatLog chat = new ChatLog();
        chat.setSender(sender);
        chat.setMessage(incoming.getMessage());
        chat.setChannel(incoming.getChannel());
        chat.setSentat(LocalDateTime.now());

        switch (incoming.getChannel()) {
            case MAP -> {
                chat.setMap(maps);
                broadcastToMap(playerPosition.getMapId(), chat, senderId);
            }
            case PRIVATE -> {
                Player receiver = playerService.findByPlayerId(incoming.getReceiverId())
                		.orElseThrow(() -> new RuntimeException("Không tìm thấy người chơi với ID: " + incoming.getReceiverId()));
                chat.setReceiver(receiver);
                chatLogService.sendMessage(chat);
                sendToPrivateUsers(senderId, incoming.getReceiverId(), chat);
            }
            case GLOBAL -> {
                broadcastToAll(chat, senderId);
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
	
	private void broadcastToMap(int mapId, ChatLog chat, int senderId) throws Exception {
	    for (Map.Entry<Integer, WebSocketSession> entry : sessions.entrySet()) {
	        int playerId = entry.getKey();
	        if (playerId == senderId) continue;

	        PlayerPositionDTO position = playerPositionService.getPositionByPlayerId(playerId)
	            .orElse(null);
	        if (position != null && position.getMapId() == mapId) {
	            send(entry.getValue(), chat);
	        }
	    }
	}
	
	private void broadcastToAll(ChatLog chat, int senderId) throws Exception {
	    for (Map.Entry<Integer, WebSocketSession> entry : sessions.entrySet()) {
	        if (entry.getKey() != senderId) {
	            send(entry.getValue(), chat);
	        }
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
