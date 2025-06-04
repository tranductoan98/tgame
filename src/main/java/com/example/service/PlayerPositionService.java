package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.dto.PlayerPositionRequest;
import com.example.entity.PlayerPosition;

public interface PlayerPositionService {
	Optional<PlayerPosition> getPositionByPlayerId(Integer playerId);
    PlayerPosition saveOrUpdatePosition(PlayerPositionRequest userPosition);
    void deletePositionByPlayerId(Integer playerId);
    List<PlayerPosition> getAllPositionsByMapId(Integer mapId);
    boolean isPlayerInMap(int playerId, int mapId);
}
