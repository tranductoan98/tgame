package com.example.handler;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionHandler {

	private final ConcurrentHashMap<Integer, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public void addSession(Integer sessionId, WebSocketSession session) {
        sessionMap.put(sessionId, session);
    }

    public void removeSession(Integer sessionId) {
        sessionMap.remove(sessionId);
    }

    public WebSocketSession getSession(Integer sessionId) {
        return sessionMap.get(sessionId);
    }

    public ConcurrentHashMap<Integer, WebSocketSession> getAllSessions() {
        return sessionMap;
    }
    
}
