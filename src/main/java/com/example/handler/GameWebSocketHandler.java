package com.example.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.dto.PlayerPositionRequest;
import com.example.entity.PlayerPosition;
import com.example.enums.Direction;
import com.example.security.JwtUtil;
import com.example.service.PlayerPositionService;
import com.example.service.TmxMapService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final JwtUtil jwtunit;
    private final PlayerPositionService playerPositionService;
    private final TmxMapService mapService;

    public GameWebSocketHandler(JwtUtil jwtunit, PlayerPositionService playerPositionService, TmxMapService mapService) {
        this.jwtunit = jwtunit;
        this.playerPositionService = playerPositionService;
		this.mapService = mapService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String token = getTokenFromQuery(query);

        if (token == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
            return;
        }
        
        try {
            int playerId = jwtunit.getPlayerIdFromToken(token);
            
            Optional<PlayerPosition> positionOptional = playerPositionService.getPositionByPlayerId(playerId);
          
            PlayerPosition playerPosition = positionOptional.get();

            int mapId = playerPosition.getMap().getMapid();
            
            session.getAttributes().put("playerId", playerId);
            session.getAttributes().put("mapId", mapId);

            PlayerSessionManager.addSession(session, playerId, mapId);

            System.out.println("Player " + playerId + " connected on map " + mapId);

        } catch (JwtException e) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
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
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(payload);

        String type = json.get("type").asText();
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
}

