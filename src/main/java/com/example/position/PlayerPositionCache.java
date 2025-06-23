package com.example.position;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.example.entity.PlayerPosition;

@Component
public class PlayerPositionCache {

    private final Map<Integer, PlayerPosition> positionMap = new ConcurrentHashMap<>();

    public void updatePosition(PlayerPosition position) {
        positionMap.put(position.getPlayerId(), position);
    }

    public void updateIfChanged(PlayerPosition position) {
        PlayerPosition current = positionMap.get(position.getPlayerId());
        if (current == null || !current.equals(position)) {
            positionMap.put(position.getPlayerId(), position);
        }
    }

    public Collection<PlayerPosition> getAllPositions() {
        return positionMap.values();
    }

    public PlayerPosition getPosition(int playerId) {
        return positionMap.get(playerId);
    }
    
    public void deletePosition(int playerId) {
    	positionMap.remove(playerId);
    }
    
    public void clear() {
        positionMap.clear();
    }
}
