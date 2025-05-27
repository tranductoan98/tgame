package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.PlayerCreateRequest;
import com.example.entity.Player;
import com.example.entity.User;
import com.example.service.PlayerService;
import com.example.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
	
	private final PlayerService playerService;
	private final UserService userService;
	
	@Autowired
    public PlayerController(PlayerService playerService, UserService userService) {
        this.playerService = playerService;
        this.userService = userService;
    }
	
	@Operation(summary = "tạo mới player", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/create")
    public ResponseEntity<?> createPlayer(@Valid @RequestBody PlayerCreateRequest request) {
		if (request.getUserId() == null) {
        	Map<String, String> error = new HashMap<>();
            error.put("message", "Bạn phải nhập userId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
		Optional<User> userOpt = userService.findByUserId(request.getUserId());
        if (userOpt.isEmpty()) {
        	Map<String, String> error = new HashMap<>();
            error.put("message", "User không tồn tại");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        Player created = playerService.createPlayer(request, userOpt);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
	
	@Operation(summary = "cập nhật player", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/update")
    public ResponseEntity<?> updatePlayer(@RequestBody Player player) {
        Optional<Player> existing = playerService.findByPlayerId(player.getPlayerid());
        if (existing.isEmpty()) {
            Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy player với userId: " + player.getPlayerid());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        Player updated = playerService.updatePlayer(player);
        return ResponseEntity.ok(updated);
    }
	
	@Operation(summary = "Xoá nhân vật theo playerId", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{playerId}")
    public ResponseEntity<?> deletePlayer(@PathVariable Integer playerId) {
        boolean deleted = playerService.deleteByPlayerId(playerId);
        if (deleted) {
            return ResponseEntity.ok("Đã xoá player có ID: " + playerId);
        } else {
            Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy player để xoá");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
	
	@Operation(summary = "Lấy danh sách tất cả player", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        if (players.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(players);
    }
	
	@Operation(summary = "Lấy player theo playerId", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/get/{userId}")
    public ResponseEntity<?> getPlayerByUserId(@PathVariable Integer playerId) {
		Optional<Player> playerOpt = playerService.findByPlayerId(playerId);
        if (playerOpt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy player với playerId: " + playerId);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(playerOpt);
    }
	
	@Operation(summary = "Lấy tất cả nhân vật của user hiện tại", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/my-players")
    public ResponseEntity<?> getMyPlayers(@PathVariable Integer userId) {
		List<Player> players = playerService.findAllByUserId(userId);
		if (players.isEmpty()) {
	    	return ResponseEntity.noContent().build();
	    }
        return ResponseEntity.ok(players);
    }
}
