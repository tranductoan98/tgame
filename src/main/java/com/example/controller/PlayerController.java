package com.example.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.dto.PlayerCreateRequest;
import com.example.dto.PlayerLoginRequest;
import com.example.dto.PlayerLoginResponse;
import com.example.dto.PlayerUpdateRequest;
import com.example.entity.Player;
import com.example.entity.User;
import com.example.security.JwtUtil;
import com.example.service.AvatarService;
import com.example.service.PlayerService;
import com.example.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
	
	private final PlayerService playerService;
	private final UserService userService;
	
	@Autowired
    private JwtUtil jwtUtil;
	
    @Autowired
    private AvatarService avatarService;
	
	@Autowired
    public PlayerController(PlayerService playerService, UserService userService, JwtUtil jwtUtil, AvatarService avatarService) {
        this.playerService = playerService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.avatarService = avatarService;
    }
	
	@Operation(summary = "tạo mới player", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/create")
    public ResponseEntity<?> createPlayer(@RequestBody PlayerCreateRequest request) {
		if (request.getUserId() == null) {
        	Map<String, String> error = new HashMap<>();
            error.put("message", "Bạn phải nhập userId");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
		Optional<User> userOpt = Optional.of(new User());
		if (request.getCharacterName() != null &&
			request.getCharacterName().length() >= 1 &&
			request.getCharacterName().length() <= 10 &&
			request.getCharacterName().matches("^[a-zA-Z0-9]+$")) {
			userOpt = userService.findByUserId(request.getUserId());
		} else {
			Map<String, String> error = new HashMap<>();
            error.put("message", "name tối thiểu 1 và tối đa 10 ký tự, không nhập ký tự đặc biệt");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
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
    public ResponseEntity<?> updatePlayer(@ModelAttribute PlayerUpdateRequest request, @RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        Optional<Player> existing = playerService.findByPlayerId(request.getPlayerid());
        if (existing.isEmpty()) {
            Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy player với userId: " + request.getPlayerid());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		String token = authHeader.substring(7);
	    Integer userId = jwtUtil.getUserIdFromToken(token);
	    
        try {
        	Player player = existing.get();
			String avatarPath = avatarService.saveAvatar(file, userId, request.getPlayerid(), player.getAvatarFace());
			request.setAvatarFace(avatarPath);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi upload avatar: " + e.getMessage());
		}
        
        Player updated = playerService.updatePlayer(request);
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
	@GetMapping("/get/{playerId}")
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
    @GetMapping("/my-players/{userId}")
    public ResponseEntity<?> getMyPlayers(@PathVariable Integer userId) {
		List<Player> players = playerService.findAllByUserId(userId);
		if (players.isEmpty()) {
	    	return ResponseEntity.noContent().build();
	    }
        return ResponseEntity.ok(players);
    }
	
	@Operation(summary = "Player Đăng nhập", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/login")
	public ResponseEntity<PlayerLoginResponse> login(@RequestBody PlayerLoginRequest request, @RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		String token = authHeader.substring(7);
		Integer userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        PlayerLoginResponse response = playerService.login(userId, request.getPlayerId());
        
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Player logout", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
		String token = authHeader.substring(7);
	    Integer userId = jwtUtil.getUserIdFromToken(token);
        playerService.logout(userId);
        return ResponseEntity.ok("Player logged out");
    }
}
