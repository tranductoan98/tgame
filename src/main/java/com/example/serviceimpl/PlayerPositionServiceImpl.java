package com.example.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.PlayerPositionDTO;
import com.example.dto.PlayerPositionRequest;
import com.example.entity.Maps;
import com.example.entity.Player;
import com.example.entity.PlayerPosition;
import com.example.enums.Direction;
import com.example.position.PlayerPositionCache;
import com.example.repository.MapRepository;
import com.example.repository.PlayerRepository;
import com.example.service.PlayerPositionService;
import com.example.repository.PlayerPositionRepository;

@Service
public class PlayerPositionServiceImpl implements PlayerPositionService{

	private final PlayerPositionRepository userPositionRepository;
	private final PlayerRepository playerRepository;
	private final MapRepository mapRepository;
	private final PlayerPositionCache positionCache;
	
	public PlayerPositionServiceImpl(PlayerPositionRepository userPositionRepository, PlayerRepository playerRepository,
			MapRepository mapRepository, PlayerPositionCache positionCache) {
		this.userPositionRepository = userPositionRepository;
		this.playerRepository = playerRepository;
		this.mapRepository = mapRepository;
		this.positionCache = positionCache;
	}

	@Override
	public Optional<PlayerPositionDTO> getPositionByPlayerId(Integer playerId) {
		PlayerPosition position = userPositionRepository.findByPlayerId(playerId);
		
		if (position == null) {
	        return Optional.empty();
	    }
		
		PlayerPositionDTO positionDTO = new PlayerPositionDTO();
		positionDTO.setUserid(position.getPlayer().getUser().getId());
		positionDTO.setPlayerId(position.getPlayerId());
		positionDTO.setMapId(position.getMap().getId());
		positionDTO.setName(position.getPlayer().getName());
		positionDTO.setGender(position.getPlayer().getGender());
		positionDTO.setLevel(position.getPlayer().getLevel());
		positionDTO.setExperience(position.getPlayer().getExperience());
		positionDTO.setGold(position.getPlayer().getGold());
		positionDTO.setDiamond(position.getPlayer().getDiamond());
		positionDTO.setAvatarFace(position.getPlayer().getAvatarFace());
		positionDTO.setCreatedAt(position.getPlayer().getCreatedAt());
		positionDTO.setStatus(position.getPlayer().getStatus());
		positionDTO.setDirection(position.getDirection());
		positionDTO.setX(position.getX());
		positionDTO.setY(position.getY());
		positionDTO.setExperienceToNextLevel(position.getPlayer().getExperienceToNextLevel());
		positionDTO.setUpdatedAt(position.getUpdatedAt());
		
		return Optional.ofNullable(positionDTO);
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
	        position.setX(2);
	        position.setY(2);
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
	    
	    positionCache.updateIfChanged(position);
	    
	    return position;
	}

	@Override
	public void deletePositionByPlayerId(Integer playerId) {
		userPositionRepository.deleteById(playerId);
		
	}

	@Override
	public List<PlayerPositionDTO> getAllPositionsByMapId(Integer mapid) {
		List<PlayerPosition> positions = userPositionRepository.findAllByMap_IdAndPlayer_Status(mapid, "online");
		
		if (positions == null || positions.isEmpty()) {
	        return Collections.emptyList();
	    }
		
		List<PlayerPositionDTO> result = new ArrayList<>();

	    for (PlayerPosition position : positions) {
	        PlayerPositionDTO dto = new PlayerPositionDTO();
	        dto.setUserid(position.getPlayer().getUser().getId());
	        dto.setPlayerId(position.getPlayerId());
	        dto.setMapId(position.getMap().getId());
	        dto.setName(position.getPlayer().getName());
	        dto.setGender(position.getPlayer().getGender());
	        dto.setLevel(position.getPlayer().getLevel());
	        dto.setExperience(position.getPlayer().getExperience());
	        dto.setGold(position.getPlayer().getGold());
	        dto.setDiamond(position.getPlayer().getDiamond());
	        dto.setAvatarFace(position.getPlayer().getAvatarFace());
	        dto.setCreatedAt(position.getPlayer().getCreatedAt());
	        dto.setStatus(position.getPlayer().getStatus());
	        dto.setDirection(position.getDirection());
	        dto.setX(position.getX());
	        dto.setY(position.getY());
	        dto.setExperienceToNextLevel(position.getPlayer().getExperienceToNextLevel());
	        dto.setUpdatedAt(position.getUpdatedAt());

	        result.add(dto);
	    }
		
		return result;
	}

	@Override
	public boolean isPlayerInMap(int playerId, int mapId) {
		return userPositionRepository.findById(playerId).map(player -> player.getMap() != null && player.getMap().getId() == mapId).orElse(false);
	}

}
