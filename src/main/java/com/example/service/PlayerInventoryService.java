package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entity.PlayerInventory;

public interface  PlayerInventoryService {
	List<PlayerInventory> getAllItemsByPlayerId(Integer playerId, boolean isEquippedCheck);

    Optional<PlayerInventory> getInventoryById(Integer id);

    PlayerInventory addItemToInventory(PlayerInventory inventory);

    PlayerInventory updateInventory(PlayerInventory inventory);

    void deleteInventoryItem(Integer id);
   
}
