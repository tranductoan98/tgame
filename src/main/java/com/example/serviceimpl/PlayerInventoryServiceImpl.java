package com.example.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.PlayerInventory;
import com.example.repository.PlayerInventoryRepository;
import com.example.service.PlayerInventoryService;

@Service
public class PlayerInventoryServiceImpl implements PlayerInventoryService{

	private final PlayerInventoryRepository playerInventoryRepository;
	
	
	public PlayerInventoryServiceImpl(PlayerInventoryRepository playerInventoryRepository) {
		this.playerInventoryRepository = playerInventoryRepository;
	}

	@Override
	public List<PlayerInventory> getAllItemsByPlayerId(Integer playerId, boolean isEquippedCheck) {
		if (isEquippedCheck) {
	        return playerInventoryRepository.findByPlayer_PlayeridAndIsEquipped(playerId, true);
	    } else {
	        return playerInventoryRepository.findByPlayer_Playerid(playerId);
	    }
	}

	@Override
	public Optional<PlayerInventory> getInventoryById(Integer id) {
		return playerInventoryRepository.findById(id);
	}

	@Override
	public PlayerInventory addItemToInventory(PlayerInventory inventory) {
		return playerInventoryRepository.save(inventory);
	}

	@Override
	public PlayerInventory updateInventory(PlayerInventory inventory) {
		 return playerInventoryRepository.save(inventory);
	}

	@Override
	public void deleteInventoryItem(Integer id) {
		playerInventoryRepository.deleteById(id);
	}

}
