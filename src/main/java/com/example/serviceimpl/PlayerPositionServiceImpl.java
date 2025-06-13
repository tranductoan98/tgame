package com.example.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.PlayerPositionRequest;
import com.example.entity.Maps;
import com.example.entity.Player;
import com.example.entity.PlayerPosition;
import com.example.enums.Direction;
import com.example.repository.MapRepository;
import com.example.repository.PlayerRepository;
import com.example.service.PlayerPositionService;
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
		
		if (optionalPlayer.isEmpty()) {
			throw new IllegalArgumentException("Player không tồn tại.");
	    }
		
		Player player = optionalPlayer.get();
		
		if (!"online".equalsIgnoreCase(player.getStatus())) {
			throw new IllegalArgumentException("Player không online.");
	    }
		
		PlayerPosition position = userPositionRepository.findByPlayerId(player.getPlayerid());
		
	    if (position == null) {
	        position = new PlayerPosition();
	        position.setPlayer(player);
	        
	        Maps map;
	        
	        if (userPosition.getMapId() != null) {
	            Optional<Maps> optionalMap = mapRepository.findById(userPosition.getMapId());
	            if (optionalMap.isPresent()) {
	                map = optionalMap.get();
	            } else {
	                map = mapRepository.findByIsDefaultTrue().orElse(null);
	            }
	        } else {
	            map = mapRepository.findByIsDefaultTrue().orElse(null);
	        }

	        if (map == null) {
	            throw new IllegalArgumentException("Không có map mặc định.");
	        }
	        
	        position.setMap(map);
	        position.setX(100);
	        position.setY(100);
	        position.setDirection(Direction.DOWN);
	        
	    }else {
	        Optional<Maps> optionalMap = mapRepository.findById(userPosition.getMapId());
	        if (optionalMap.isEmpty()) {
	        	throw new IllegalArgumentException("Map không tồn tại.");
	        }

	        position.setMap(optionalMap.get());
	        position.setX(userPosition.getX());
	        position.setY(userPosition.getY());
	        position.setDirection(userPosition.getDirection());
	    }
	    
	    return userPositionRepository.save(position);
	}

	@Override
	public void deletePositionByPlayerId(Integer playerId) {
		userPositionRepository.deleteById(playerId);
		
	}

	@Override
	public List<PlayerPosition> getAllPositionsByMapId(Integer mapid) {
		return userPositionRepository.findAllByMap_IdAndPlayer_Status(mapid, "online");
	}

	@Override
	public boolean isPlayerInMap(int playerId, int mapId) {
		return userPositionRepository.findById(playerId).map(player -> player.getMap() != null && player.getMap().getId() == mapId).orElse(false);
	}

}
