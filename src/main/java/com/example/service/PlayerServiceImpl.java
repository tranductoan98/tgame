package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.PlayerCreateRequest;
import com.example.dto.PlayerLoginResponse;
import com.example.dto.PlayerUpdateRequest;
import com.example.entity.Maps;
import com.example.entity.Player;
import com.example.entity.PlayerPosition;
import com.example.entity.User;
import com.example.enums.Direction;
import com.example.repository.MapRepository;
import com.example.repository.PlayerPositionRepository;
import com.example.repository.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService{

	private final PlayerRepository playerRepository;
	private final PlayerPositionRepository playerPositionRepository;
	private final MapRepository mapRepository;
	
	public PlayerServiceImpl(PlayerRepository playerRepository, PlayerPositionRepository playerPositionRepository, MapRepository mapRepository) {
		this.playerRepository = playerRepository;
		this.playerPositionRepository = playerPositionRepository;
		this.mapRepository = mapRepository;
	}

	@Override
	public Player createPlayer(PlayerCreateRequest playerrq, Optional<User> user) {
		Player player = new Player();
        player.setUser(user.get());
        player.setName(playerrq.getCharacterName());
        player.setGender(playerrq.getGender());
        player.setCreatedAt(LocalDateTime.now());
		return playerRepository.save(player);
	}

	@Override
	public Player updatePlayer(PlayerUpdateRequest updatedPlayer) {
		Optional<Player> optionalPlayer = playerRepository.findById(updatedPlayer.getPlayerid());
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            player.setName(updatedPlayer.getName());
            player.setGender(updatedPlayer.getGender());
            player.setLevel(updatedPlayer.getLevel());
            player.setExperience(updatedPlayer.getExperience());
            player.setGold(updatedPlayer.getGold());
            player.setDiamond(updatedPlayer.getDiamond());
            player.setAvatarFace(updatedPlayer.getAvatarFace());
            return playerRepository.save(player);
        }
        return null;
	}

	@Override
	public boolean deleteByPlayerId(Integer playerId) {
		if (playerRepository.existsById(playerId)) {
            playerRepository.deleteById(playerId);
            return true;
        }
		return false;
	}

	@Override
	public List<Player> findAllByUserId(Integer userid) {
		return playerRepository.findAllByUserId(userid);
	}

	@Override
	public Optional<Player> findByPlayerId(Integer playerid) {
		return playerRepository.findById(playerid);
	}

	@Override
	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}

	@Override
	public PlayerLoginResponse login(Integer userId, Integer playerId) {
		Optional<Player> playerOpt  = playerRepository.findByPlayeridAndUserId(playerId, userId);
		if (playerOpt .isEmpty()) return null;
		
		Player player = playerOpt.get();
        player.setStatus("online");
        playerRepository.save(player);
        Maps maps = mapRepository.findById(1).get();
        
        PlayerPosition position = playerPositionRepository.findByPlayerId(playerId);
        if (position == null) {
            position = new PlayerPosition();
            position.setPlayer(player);
            position.setMap(maps);
            position.setX(100);
            position.setY(100);
            position.setDirection(Direction.DOWN);
        }
        playerPositionRepository.save(position);
        
        PlayerLoginResponse response = new PlayerLoginResponse();
        response.setPlayerId(player.getPlayerid());
        response.setPlayerName(player.getName());
        response.setStatus(player.getStatus());
        response.setMapId(1);
        response.setX(100);
        response.setY(100);
        response.setDirection(Direction.DOWN);

        return response;
	}

	@Override
	public void logout(Integer playerId) {
		 Player player = playerRepository.findById(playerId)
		            .orElseThrow(() -> new RuntimeException("Player not found"));

        player.setStatus("offline");
        playerRepository.save(player);
		
	}
}
