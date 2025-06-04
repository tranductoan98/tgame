package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.entity.Maps;
import com.example.enums.MapType;

public interface MapService {
	Maps createMap(Maps map);
	List<Maps> getAllMaps();
	Optional<Maps> getMapById(Integer mapId);
	Maps updateMap(Maps updatedMap);
	boolean deleteByMapId(Integer mapId);
	List<Maps> getMapsByType(MapType type);
}
