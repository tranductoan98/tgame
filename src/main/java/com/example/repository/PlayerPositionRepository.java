package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.PlayerPosition;

@Repository
public interface PlayerPositionRepository extends JpaRepository<PlayerPosition, Integer> {
    PlayerPosition findByPlayerId(Integer playerId);
    boolean existsByPlayerId(Integer playerId);
    List<PlayerPosition> findAllByMap_IdAndPlayer_Status(Integer mapid, String status);

}
