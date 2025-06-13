package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Maps;
import com.example.enums.MapType;
import com.example.service.MapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/map")
public class MapController {
	
	private final MapService mapService;
	
//	@Autowired
//	private MapFileService mapFileService;

	public MapController(MapService mapService) {
		this.mapService = mapService;
	}
	
	@Operation(summary = "Tạo bản đồ mới", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping(value = "/create")
	public ResponseEntity<?> createMap(@RequestBody Maps map){
//		try {
//			String mapFilePath = mapFileService.saveMapFile(file, map);
//			map.setMapFile(mapFilePath);
//			Maps createdMap = mapService.createMap(map);
//	        return ResponseEntity.ok(createdMap);
//		} catch (IOException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi upload maps: " + e.getMessage());
//		}

		Maps createdMap = mapService.createMap(map);
		return ResponseEntity.ok(createdMap);
	}
	
	@Operation(summary = "get bản đồ theo type hoặc tất cả", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/get/")
    public ResponseEntity<List<Maps>> getAllMaps(@RequestParam(required = false) String type) {
        if (type == null) {
            return ResponseEntity.ok(mapService.getAllMaps());
        } else {
            try {
                MapType mapType = MapType.valueOf(type.toUpperCase());
                return ResponseEntity.ok(mapService.getMapsByType(mapType));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
    }
	
	@Operation(summary = "get theo id bản đồ", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/{mapid}")
    public ResponseEntity<?> getMapById(@PathVariable Integer mapid) {
		Optional<Maps> mapOpt = mapService.getMapById(mapid);
        if (mapOpt.isEmpty()) {
        	Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy map với mapId: " + mapid);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(mapOpt);
    }
	
	@Operation(summary = "Update bản đồ mới", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping(value = "/update")
	public ResponseEntity<?> updateMap(@ModelAttribute Maps map){
//		try {
//			String mapFilePath = mapFileService.saveMapFile(file, map);
//			map.setMapFile(mapFilePath);
//		} catch (IOException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi upload maps: " + e.getMessage());
//		}
		
		Maps createdMap = mapService.updateMap(map);
        return ResponseEntity.ok(createdMap);
	}
	
	@Operation(summary = "Xoá bản đồ theo mapid", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{mapid}")
    public ResponseEntity<?> deletePlayer(@PathVariable Integer mapid) {
        boolean deleted = mapService.deleteById(mapid);
        if (deleted) {
            return ResponseEntity.ok("Đã xoá bản đồ có ID: " + mapid);
        } else {
            Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy bản đồ để xoá");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
