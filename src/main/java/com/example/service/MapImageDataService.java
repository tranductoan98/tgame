package com.example.service;

import java.util.List;

import com.example.entity.MapImageData;

public interface MapImageDataService {
	List<MapImageData> getByMapId(Integer mapId);
    List<MapImageData> findByMapIdAndType(Integer mapId, String type);
}
