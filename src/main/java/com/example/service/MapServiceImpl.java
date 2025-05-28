package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Maps;
import com.example.enums.MapType;
import com.example.repository.MapRepository;

@Service
public class MapServiceImpl implements MapService{

	private final MapRepository mapRepository;
	
	public MapServiceImpl(MapRepository mapRepository) {
		this.mapRepository = mapRepository;
	}

	@Override
	public Maps createMap(Maps map) {
		return mapRepository.save(map);
	}

	@Override
	public List<Maps> getAllMaps() {
		 return mapRepository.findAll();
	}

	@Override
	public Optional<Maps> getMapById(Integer mapId) {
		return mapRepository.findById(mapId);
	}

	@Override
	public Maps updateMap(Maps updatedMap) {
		Optional<Maps> existingMap = mapRepository.findById(updatedMap.getMapid());
		if (existingMap.isPresent()) {
			Maps map = existingMap.get();
			map.setName(updatedMap.getName());
			map.setMapFile(updatedMap.getMapFile());
			map.setType(updatedMap.getType());
			map.setSpawnX(updatedMap.getSpawnX());
			map.setSpawnY(updatedMap.getSpawnY());
			return mapRepository.save(map);
		}
        return null;
	}

	@Override
	public boolean deleteByMapId(Integer mapId) {
		if (mapRepository.existsById(mapId)) {
			mapRepository.deleteById(mapId);
            return true;
        }
		return false;
	}
	
	@Override
	public List<Maps> getMapsByType(MapType type) {
        return mapRepository.findByType(type);
    }

}
