package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AuthResponse;
import com.example.dto.UserRegisterRequest;
import com.example.entity.User;
import com.example.security.JwtUtil;
import com.example.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserService userService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
	
	@Operation(summary = "Đăng ký user mới")
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
		System.out.println("name: " + request.getUsername());
		
		if (request.getUsername() == null) {
        	Map<String, String> error = new HashMap<>();
            error.put("message", "Bạn phải nhập User name và password");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        if (userService.findByUsername(request.getUsername()) != null) {
            Map<String, String> error = new HashMap<>();
	        error.put("message", "Username đãn được sử dụng: " + request.getUsername());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        User savedUser = userService.register(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok(savedUser);
    }
	
	@Operation(summary = "Đăng nhập")
	@PostMapping("/login")
    public ResponseEntity<?> login(String username, String password) {
		try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai username hoặc password");
        }
    }
	
	@Operation(summary = "Xoá user theo ID", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUserById(@PathVariable Integer userId) {
	    boolean deleted = userService.deleteByUserId(userId);
	    if (deleted) {
	        return ResponseEntity.ok().body("Xoá user thành công với ID = " + userId);
	    } else {
	        Map<String, String> error = new HashMap<>();
            error.put("message", "Không tìm thấy user với ID = "  + userId);
            return ResponseEntity.badRequest().body(error); 
	    }
	}
	
	@Operation(summary = "Cập nhật mật khẩu", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/update-password")
	public ResponseEntity<?> updatePassword(@RequestParam Integer userId,
	                                        @RequestParam String oldPassword,
	                                        @RequestParam String newPassword) {
	    boolean updated = userService.updatePassword(userId, oldPassword, newPassword);
	    if (updated) {
	        return ResponseEntity.ok().body("Đổi mật khẩu thành công");
	    } else {
	        Map<String, String> error = new HashMap<>();
            error.put("message", "Mật khẩu cũ không đúng hoặc user không tồn tại");
            return ResponseEntity.badRequest().body(error); 
	    }
	}
	
	@Operation(summary = "Lấy danh sách user", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/users")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
		if (authentication == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập");
	    }
		
		String username = authentication.getName();
	    User currentUserOpt = userService.findByUsername(username);
	    if (currentUserOpt.getUsername() == null) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User không hợp lệ");
	    }
	    
        List<User> users = userService.getAllUsers();
        
        if (users == null || users.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Danh sách user rỗng");
            return ResponseEntity.badRequest().body(error); 
        }
        
        return ResponseEntity.ok(users);
    }
	
	@Operation(summary = "Lấy user theo username", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication authentication,@RequestParam String username) {
		if (authentication == null || !authentication.isAuthenticated()) {
	        Map<String, String> error = new HashMap<>();
	        error.put("message", "Bạn chưa đăng nhập hoặc token không hợp lệ");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	    }
		
	    User currentUserOpt = userService.findByUsername(username);
	    if (currentUserOpt == null) {
	        Map<String, String> error = new HashMap<>();
	        error.put("message", "Không tìm thấy user: " + username);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	    }
	    
        return ResponseEntity.ok(currentUserOpt);
    }
}

