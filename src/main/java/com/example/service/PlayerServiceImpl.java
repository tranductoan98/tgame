package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dto.PlayerCreateRequest;
import com.example.entity.Player;
import com.example.entity.User;
import com.example.repository.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService{

	private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

	@Override
	public Player createPlayer(PlayerCreateRequest playerrq, Optional<User> user) {
		Player player = new Player();
        player.setUerid(user.get());
        player.setName(playerrq.getCharacterName());
        player.setGender(playerrq.getGender());
        player.setCreatedAt(LocalDateTime.now());
		return playerRepository.save(player);
	}

	@Override
	public Player updatePlayer(Player updatedPlayer) {
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

}
