package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.dto.PlayerCreateRequest;
import com.example.dto.PlayerLoginResponse;
import com.example.dto.PlayerUpdateRequest;
import com.example.entity.Player;
import com.example.entity.User;

public interface PlayerService {
	Player createPlayer(PlayerCreateRequest player, Optional<User> userOpt);
	Player updatePlayer(PlayerUpdateRequest player);
	boolean deleteByPlayerId(Integer playerid);
	List<Player> findAllByUserId(Integer userId);
	List<Player> getAllPlayers();
	Optional<Player> findByPlayerId(Integer playerid);
	PlayerLoginResponse login(Integer userId, Integer playerId);
	void logout(Integer playerId);
}
