package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.entity.MapImageData;
import com.example.service.MapImageDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/map-image-data")
public class MapImageDataController {

	private final MapImageDataService mapImageDataService;
	
	@Autowired
    public MapImageDataController(MapImageDataService mapImageDataService) {
        this.mapImageDataService = mapImageDataService;
    }
	
	@Operation(summary = "Lấy toàn bộ tile của 1 map", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/map/{mapId}")
    public List<MapImageData> getByMapId(@PathVariable Integer mapId) {
        return mapImageDataService.getByMapId(mapId);
    }
	
	@Operation(summary = "Lấy tile theo mapId + type", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/map/{mapId}/layer/{type}")
    public List<MapImageData> getByMapIdAndLayer(@PathVariable Integer mapId, @PathVariable String type) {
        return mapImageDataService.findByMapIdAndType(mapId, type);
    }
}
