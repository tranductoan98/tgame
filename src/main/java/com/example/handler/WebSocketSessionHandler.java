package com.example.handler;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionHandler {

	private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public void addSession(String sessionId, WebSocketSession session) {
        sessionMap.put(sessionId, session);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public WebSocketSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public ConcurrentHashMap<String, WebSocketSession> getAllSessions() {
        return sessionMap;
    }
    
}
