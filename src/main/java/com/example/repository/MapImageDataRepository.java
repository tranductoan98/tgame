package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.MapImageData;

@Repository
public interface MapImageDataRepository extends JpaRepository<MapImageData, Integer> {
    List<MapImageData> findByMapId(Integer mapId);
    List<MapImageData> findByMapIdAndType(Integer mapId, String type);
}
