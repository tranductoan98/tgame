package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.PlayerPositionRequest;
import com.example.entity.Maps;
import com.example.entity.Player;
import com.example.entity.PlayerPosition;
import com.example.repository.MapRepository;
import com.example.repository.PlayerRepository;
import com.example.repository.PlayerPositionRepository;

@Service
public class PlayerPositionServiceImpl implements PlayerPositionService{

	private final PlayerPositionRepository userPositionRepository;
	private final PlayerRepository playerRepository;
	private final MapRepository mapRepository;
	
	public PlayerPositionServiceImpl(PlayerPositionRepository userPositionRepository, PlayerRepository playerRepository,
			MapRepository mapRepository) {
		this.userPositionRepository = userPositionRepository;
		this.playerRepository = playerRepository;
		this.mapRepository = mapRepository;
	}

	@Override
	public Optional<PlayerPosition> getPositionByPlayerId(Integer playerId) {
		return Optional.ofNullable(userPositionRepository.findByPlayerId(playerId));
	}

	@Override
	public PlayerPosition saveOrUpdatePosition(PlayerPositionRequest userPosition) {
		Optional<Player> optionalPlayer = playerRepository.findById(userPosition.getPlayerId());
		Optional<Maps> optionalMap = mapRepository.findById(userPosition.getMapId());
		
		if (optionalPlayer.isEmpty() || optionalMap.isEmpty()) {
	        return null;
	    }
		
		Player player = optionalPlayer.get();
		
		
		if (!"online".equalsIgnoreCase(player.getStatus())) {
	        return null;
	    }
		
		Maps map = optionalMap.get();
		
		PlayerPosition position = userPositionRepository.findByPlayerId(player.getPlayerid());
	    if (position == null) {
	        position = new PlayerPosition();
	        position.setPlayer(player);
	    }
	    
	    position.setMap(map);
	    position.setX(userPosition.getX());
	    position.setY(userPosition.getY());
	    position.setDirection(userPosition.getDirection());
	    
	    return userPositionRepository.save(position);
	}

	@Override
	public void deletePositionByPlayerId(Integer playerId) {
		userPositionRepository.deleteById(playerId);
		
	}

	@Override
	public List<PlayerPosition> getAllPositionsByMapId(Integer mapid) {
		return userPositionRepository.findAllByMap_MapidAndPlayer_Status(mapid, "online");
	}

	@Override
	public boolean isPlayerInMap(int playerId, int mapId) {
		return userPositionRepository.findById(playerId).map(player -> player.getMap() != null && player.getMap().getMapid() == mapId).orElse(false);
	}

}
