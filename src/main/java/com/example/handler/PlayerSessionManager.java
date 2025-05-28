package com.example.handler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;

public class PlayerSessionManager {

    private static final Map<Integer, WebSocketSession> playerSessions = new ConcurrentHashMap<>();

    private static final Map<Integer, Set<WebSocketSession>> mapSessions = new ConcurrentHashMap<>();

    public static void addSession(WebSocketSession session, int playerId, int mapId) {
        playerSessions.put(playerId, session);

        mapSessions.computeIfAbsent(mapId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    public static void removeSession(WebSocketSession session, int playerId, int mapId) {
        playerSessions.remove(playerId);

        Set<WebSocketSession> sessions = mapSessions.get(mapId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                mapSessions.remove(mapId);
            }
        }
    }

    public static Set<WebSocketSession> getSessionsInMap(int mapId) {
        return mapSessions.getOrDefault(mapId, Set.of());
    }

    public static WebSocketSession getSessionByPlayerId(int playerId) {
        return playerSessions.get(playerId);
    }
}