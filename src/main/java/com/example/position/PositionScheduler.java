package com.example.position;

import java.util.Collection;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.entity.PlayerPosition;
import com.example.repository.PlayerPositionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PositionScheduler {

    private final PlayerPositionRepository positionRepository;
    private final PlayerPositionCache positionCache;
    
    public PositionScheduler(PlayerPositionRepository positionRepository, PlayerPositionCache positionCache) {
		this.positionRepository = positionRepository;
		this.positionCache = positionCache;
	}

	@Scheduled(fixedRate = 5000)
    public void savePositionsToDB() {
		Collection<PlayerPosition> positions = positionCache.getAllPositions();
	    if (!positions.isEmpty()) {
	        positionRepository.saveAll(positions);
	    }
    }
}