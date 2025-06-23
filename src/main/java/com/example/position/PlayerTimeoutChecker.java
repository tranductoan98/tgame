package com.example.position;

import java.util.Map;
import java.util.Set;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.example.handler.GameWebSocketHandler;
import com.example.handler.PlayerSessionManager;

@Component
@EnableScheduling
public class PlayerTimeoutChecker {

    private final GameWebSocketHandler webSocketHandler;

    public PlayerTimeoutChecker(GameWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Scheduled(fixedRate = 1000)
    public void checkInactivePlayers() {
        long now = System.currentTimeMillis();
        Map<Integer, Long> sessionLastSeen = webSocketHandler.getSessionLastSeen();
        Set<Integer> activePlayers = webSocketHandler.getActivePlayerIds();

        for (Map.Entry<Integer, Long> entry : sessionLastSeen.entrySet()) {
            if (now - entry.getValue() > 5000) {
                WebSocketSession session = PlayerSessionManager.getSessionByPlayerId(entry.getKey());
                if (session != null && session.isOpen()) {
                    Integer playerId = (Integer) session.getAttributes().get("playerId");
                    if (playerId != null && activePlayers.contains(playerId)) {
                        activePlayers.remove(playerId);
                        sessionLastSeen.remove(playerId);
                    }
                }
            }
        }
    }
}