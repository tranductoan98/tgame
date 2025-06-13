package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.PlayerInventory;

@Repository
public interface PlayerInventoryRepository extends JpaRepository<PlayerInventory, Integer> {
	 List<PlayerInventory> findByPlayer_Playerid(Integer playerId);
	 List<PlayerInventory> findByPlayer_PlayeridAndIsEquipped(Integer playerId, boolean isEquipped);

}
