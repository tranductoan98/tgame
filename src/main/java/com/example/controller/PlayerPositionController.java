package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.PlayerPositionDTO;
import com.example.dto.PlayerPositionRequest;
import com.example.entity.PlayerPosition;
import com.example.service.PlayerPositionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/player-position")
public class PlayerPositionController {
	
	private final PlayerPositionService positionService;

	public PlayerPositionController(PlayerPositionService positionService) {
		super();
		this.positionService = positionService;
	}
	
	@Operation(summary = "lấy vị trí người chơi", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/{playerId}")
    public ResponseEntity<?> getPositionByPlayerId(@PathVariable Integer playerId) {
        Optional<PlayerPositionDTO> positionOpt = positionService.getPositionByPlayerId(playerId);
        
        if (positionOpt == null) {
	        Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy user: " + playerId);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	    }
        return ResponseEntity.ok(positionOpt);
        
    }
	
	@Operation(summary = "lấy vị trí tất cả người chơi trên một map", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/map/{mapid}")
    public ResponseEntity<List<PlayerPositionDTO>> getAllPositionsByMap(@PathVariable Integer mapid) {
        List<PlayerPositionDTO> positions = positionService.getAllPositionsByMapId(mapid);
        return ResponseEntity.ok(positions);
    }
	
	@Operation(summary = "Tạo hoặc cập nhật vị trí người chơi", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/player/")
    public ResponseEntity<PlayerPosition> saveOrUpdatePosition(@RequestBody PlayerPositionRequest playerPosition) {
        PlayerPosition saved = positionService.saveOrUpdatePosition(playerPosition);
        return ResponseEntity.ok(saved);
    }
	
	@Operation(summary = "Xóa vị trí người chơi theo playerId", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePosition(@PathVariable Integer playerId) {
		positionService.deletePositionByPlayerId(playerId);
        return ResponseEntity.noContent().build();
    }
}
