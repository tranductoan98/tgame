package com.example.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.dto.PlayerPositionRequest;
import com.example.entity.Player;
import com.example.entity.PlayerPosition;
import com.example.enums.Direction;
import com.example.security.JwtUtil;
import com.example.service.PlayerPositionService;
import com.example.service.PlayerService;
import com.example.service.TmxMapService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

	private final PlayerService playerService;
    private final PlayerPositionService playerPositionService;
    private final WebSocketSessionHandler sessionManager;
    private final TmxMapService mapService;
    private final JwtUtil jwtUtil;
    
    private ConcurrentHashMap<String, Long> sessionLastSeen = new ConcurrentHashMap<>();

    public GameWebSocketHandler(PlayerPositionService playerPositionService, TmxMapService mapService, PlayerService playerService, JwtUtil jwtUtil, WebSocketSessionHandler sessionManager) {
        this.playerService = playerService;
		this.playerPositionService = playerPositionService;
		this.sessionManager = sessionManager;
		this.mapService = mapService;
		this.jwtUtil = jwtUtil;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("✅ WebSocket connected: " + session.getId());

        String query = session.getUri().getQuery();
        String token = getTokenFromQuery(query);
        String playerIdParam = getQueryParam(query, "playerId");

        if (token == null || token.isBlank()) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
            System.out.println("❌ Connection rejected: Missing token.");
            return;
        }

        try {
        	
        	int playerId = Integer.parseInt(playerIdParam);
        	int userId = jwtUtil.getUserIdFromToken(token);
        	
        	List<Player> players = playerService.findAllByUserId(userId);
        	
        	boolean exists = players.stream().anyMatch(p -> p.getPlayerid() == playerId);
        	
        	if (!exists) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("PlayerId không thuộc User này"));
                return;
            }
            
            Optional<PlayerPosition> positionOptional = playerPositionService.getPositionByPlayerId(playerId);
            if (positionOptional.isEmpty()) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Player position not found"));
                System.out.println("❌ Player " + playerId + " has no position.");
                return;
            }

            PlayerPosition playerPosition = positionOptional.get();
            int mapId = playerPosition.getMap().getId();

            session.getAttributes().put("playerId", playerId);
            session.getAttributes().put("mapId", mapId);

            PlayerSessionManager.addSession(session, playerId, mapId);
        	sessionManager.addSession(session.getId(), session);

            System.out.println("✅ Player " + playerId + " connected on map " + mapId);

        } catch (JwtException e) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
            System.out.println("❌ Invalid token: " + e.getMessage());
        } catch (Exception e) {
            session.close(CloseStatus.SERVER_ERROR.withReason("Internal error"));
            System.out.println("❌ WebSocket error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getTokenFromQuery(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals("token")) {
                return kv[1];
            }
        }
        return null;
    }
    
    private String getQueryParam(String query, String key) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] parts = param.split("=");
            if (parts.length == 2 && parts[0].equals(key)) {
                return parts[1];
            }
        }
        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer playerId = (Integer) session.getAttributes().get("playerId");
        Integer mapId = (Integer) session.getAttributes().get("mapId");

        if (playerId != null && mapId != null) {            PlayerSessionManager.removeSession(session, playerId, mapId);
            System.out.println("Player " + playerId + " disconnected from map " + mapId);
        }
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	sessionLastSeen.put(session.getId(), System.currentTimeMillis());
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(payload);

        String type = json.get("type").asText();
        
        System.out.println("type: " + type);
        
        if ("ping".equals(type)) {
            sessionLastSeen.put(session.getId(), System.currentTimeMillis());
            return;
        }
        
        if ("move".equals(type)) {
            int playerId = (Integer) session.getAttributes().get("playerId");
            int x = json.get("x").asInt();
            int y = json.get("y").asInt();
            String dirStr = json.get("direction").asText();
            int mapId = (Integer) session.getAttributes().get("mapId");
            
            if (!mapService.isPositionValid(mapId, x, y)) {
                System.out.println("Vị trí không hợp lệ: (" + x + ", " + y + ") trên map " + mapId);
                return;
            }
            
            Direction direction = Direction.valueOf(dirStr.toUpperCase());
            
            PlayerPositionRequest playerPosition = new PlayerPositionRequest();
            playerPosition.setPlayerId(playerId);
            playerPosition.setMapId(mapId);
            playerPosition.setX(x);
            playerPosition.setY(y);
            playerPosition.setDirection(direction);
            playerPositionService.saveOrUpdatePosition(playerPosition);

            Set<WebSocketSession> sessions = PlayerSessionManager.getSessionsInMap(mapId);

            String updateMessage = mapper.writeValueAsString(Map.of(
                "type", "update_move",
                "playerId", playerId,
                "x", x,
                "y", y,
                "direction", direction
            ));

            TextMessage updateText = new TextMessage(updateMessage);

            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(updateText);
                }
            }
        }
    }
    
    @Scheduled(fixedRate = 30000)
	public void checkInactiveSessions() {
	    long now = System.currentTimeMillis();
	    for (Map.Entry<String, Long> entry : sessionLastSeen.entrySet()) {
	        if (now - entry.getValue() > 60000) { 
	            WebSocketSession session = sessionManager.getSession(entry.getKey());
	            if (session != null && session.isOpen()) {
	                try {
	                    session.close();
	                    System.out.println("Session timeout closed: " + session.getId());
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
	}
}

