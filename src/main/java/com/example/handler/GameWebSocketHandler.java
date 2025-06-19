package com.example.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.dto.PlayerPositionDTO;
import com.example.dto.PlayerPositionRequest;
import com.example.entity.Player;
import com.example.enums.Direction;
import com.example.position.PlayerPositionCache;
import com.example.security.JwtUtil;
import com.example.service.PlayerPositionService;
import com.example.service.PlayerService;
import com.example.service.MapCollisionService;
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
    private final MapCollisionService mapService;
    private final WebSocketSessionHandler sessionManager;
    private final JwtUtil jwtUtil;
    private final Set<Integer> activePlayerIds = ConcurrentHashMap.newKeySet();
    private final PlayerPositionCache positionCache;
    
    private ConcurrentHashMap<Integer, Long> sessionLastSeen = new ConcurrentHashMap<>();

    public GameWebSocketHandler(PlayerPositionService playerPositionService, MapCollisionService mapService, PlayerService playerService, JwtUtil jwtUtil, WebSocketSessionHandler sessionManager, MapCollisionService mapCollisionService, PlayerPositionCache positionCache) {
        this.playerService = playerService;
		this.playerPositionService = playerPositionService;
		this.sessionManager = sessionManager;
		this.mapService = mapService;
		this.jwtUtil = jwtUtil;
		this.positionCache = positionCache;
    }
    
    public Map<Integer, Long> getSessionLastSeen() {
        return sessionLastSeen;
    }

    public Set<Integer> getActivePlayerIds() {
        return activePlayerIds;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String token = getTokenFromQuery(query);
        String playerIdParam = getQueryParam(query, "playerId");

        if (token == null || token.isBlank()) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
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
            
            Optional<PlayerPositionDTO> positionOptional = playerPositionService.getPositionByPlayerId(playerId);
            if (positionOptional.isEmpty()) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Player position not found"));
                return;
            }

            PlayerPositionDTO playerPosition = positionOptional.get();
            int mapId = playerPosition.getMapId();

            session.getAttributes().put("playerId", playerId);
            session.getAttributes().put("mapId", mapId);

            PlayerSessionManager.addSession(session, playerId, mapId);
        	sessionManager.addSession(playerId, session);

        } catch (JwtException e) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
        } catch (Exception e) {
            session.close(CloseStatus.SERVER_ERROR.withReason("Internal error"));
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

        if (playerId != null && mapId != null) PlayerSessionManager.removeSession(session, playerId, mapId);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Integer playerId = (Integer) session.getAttributes().get("playerId");
        Integer mapId = (Integer) session.getAttributes().get("mapId");

        if (playerId == null || mapId == null) {
            System.out.println("Thiếu playerId hoặc mapId trong session");
            return;
        }

        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json;

        try {
            json = mapper.readTree(payload);
        } catch (Exception e) {
            return;
        }

        String type = json.get("type").asText();
        System.out.println("type " + type);

        if ("ping".equals(type)) {
            sessionLastSeen.put(playerId, System.currentTimeMillis());
            return;
        }

        if ("move".equals(type)) {
            long now = System.currentTimeMillis();
            float x = (float) json.get("x").asDouble();
            float y = (float) json.get("y").asDouble();
            
            String dirStr = json.get("direction").asText();

            if (!mapService.isMapLoaded(mapId)) {
                try {
                    mapService.loadFromDatabase(mapId);
                } catch (Exception e) {
                    return;
                }
            }

            if (!mapService.isPositionValid(mapId, x, y)) {
                System.out.println("Vị trí di chuyển không hợp lệ của người chơi: " + playerId + ": (" + x + ", " + y + ")");
                return;
            }

            Direction direction;
            try {
                direction = Direction.valueOf(dirStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Hướng không hợp lệ: " + dirStr);
                return;
            }

            sessionLastSeen.put(playerId, now);
            activePlayerIds.add(playerId);

            PlayerPositionRequest playerPosition = new PlayerPositionRequest();
            playerPosition.setPlayerId(playerId);
            playerPosition.setMapId(mapId);
            playerPosition.setX(x);
            playerPosition.setY(y);
            playerPosition.setDirection(direction);
            playerPositionService.saveOrUpdatePosition(playerPosition);

            String updateMessage = mapper.writeValueAsString(Map.of(
                "type", "update_move",
                "playerId", playerId,
                "x", x,
                "y", y,
                "direction", direction
            ));

            TextMessage updateText = new TextMessage(updateMessage);
            Set<WebSocketSession> sessions = PlayerSessionManager.getSessionsInMap(mapId);

            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(updateText);
                }
            }
        }
    }
    
    public void sendPlayerOnline(int playerId) {
        Optional<Player> playerOpt = playerService.findByPlayerId(playerId);
        Optional<PlayerPositionDTO> positionOpt = playerPositionService.getPositionByPlayerId(playerId);

        if (playerOpt.isEmpty() || positionOpt.isEmpty()) return;

        Player player = playerOpt.get();
        PlayerPositionDTO pos = positionOpt.get();
        int mapId = pos.getMapId();

        Map<String, Object> message = Map.of(
            "type", "player_online",
            "playerId", player.getPlayerid(),
            "playerName", player.getName(),
            "x", pos.getX(),
            "y", pos.getY(),
            "direction", pos.getDirection().name()
        );

        try {
            String json = new ObjectMapper().writeValueAsString(message);
            TextMessage textMessage = new TextMessage(json);

            Set<WebSocketSession> sessions = PlayerSessionManager.getSessionsInMap(mapId);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @Scheduled(fixedRate = 30000)
	public void checkInactiveSessions() {
	    long now = System.currentTimeMillis();
	    for (Map.Entry<Integer, Long> entry : sessionLastSeen.entrySet()) {
	    	int playerId = entry.getKey();
	        long lastSeen = entry.getValue();
	        
	        if (now - lastSeen > 60000) { 
	            WebSocketSession session = sessionManager.getSession(entry.getKey());
	            if (session != null && session.isOpen()) {
	                try {
	                    session.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            
	            positionCache.deletePosition(playerId);
	            sessionLastSeen.remove(playerId);
	            activePlayerIds.remove(playerId);
	            playerService.logout(playerId);
	            System.out.println("Đã xóa người chơi không hoạt động: " + playerId);
	        }
	    }
	}
}

