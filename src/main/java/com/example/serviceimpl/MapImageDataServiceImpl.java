package com.example.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.MapImageData;
import com.example.repository.MapImageDataRepository;
import com.example.service.MapImageDataService;

@Service
public class MapImageDataServiceImpl implements MapImageDataService {

    private final MapImageDataRepository mapImageDataRepository;

    @Autowired
    public MapImageDataServiceImpl(MapImageDataRepository mapImageDataRepository) {
        this.mapImageDataRepository = mapImageDataRepository;
    }

    @Override
    public List<MapImageData> getByMapId(Integer mapId) {
        return mapImageDataRepository.findByMapId(mapId);
    }

    @Override
    public List<MapImageData> findByMapIdAndType(Integer mapId, String type) {
        return mapImageDataRepository.findByMapIdAndType(mapId, type);
    }
}
