package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
	List<Player> findAllByUserId(Integer userId);
}
