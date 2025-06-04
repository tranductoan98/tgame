package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.dto.ChatLogDTO;
import com.example.entity.ChatLog;
import com.example.enums.ChatChannel;
import com.example.service.ChatLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/chat")
public class ChatLogController {

	private final ChatLogService chatLogService;
	
	@Autowired
    public ChatLogController(ChatLogService chatLogService) {
        this.chatLogService = chatLogService;
    }
	
	@Operation(summary = "Gửi tin nhắn", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/send")
    public ChatLogDTO sendMessage(@RequestBody ChatLog chatLog) {
        ChatLog saved = chatLogService.sendMessage(chatLog);
        return ChatLogDTO.fromEntity(saved);
    }
	
	@Operation(summary = "Lấy lịch sử chat trong map (phân trang)", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/map/{mapId}")
    public List<ChatLogDTO> getMapChat(@PathVariable int mapId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatLogService.getChatByMap(mapId, pageable).getContent().stream().map(ChatLogDTO::fromEntity).toList();
    }
	
	@Operation(summary = "Lấy chat riêng giữa 2 người chơi (chat 2 chiều)", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/private")
    public List<ChatLogDTO> getPrivateChat(@RequestParam int player1Id, @RequestParam int player2Id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatLogService.getPrivateChat(player1Id, player2Id, pageable).getContent().stream().map(ChatLogDTO::fromEntity).toList();
    }
	
	@Operation(summary = "Lấy 100 dòng chat mới nhất trong map", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/map/{mapId}/latest")
    public List<ChatLogDTO> getLatestChatInMap(@PathVariable int mapId, @RequestParam(defaultValue = "100") int limit) {
        return chatLogService.getLatestChatInMap(mapId, limit).stream().map(ChatLogDTO::fromEntity).toList();
    }
	
	@Operation(summary = "Lấy channel", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/channel")
    public List<ChatLogDTO> getChatByChannel(@RequestParam ChatChannel channel, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatLogService.getChatByChannel(channel, pageable).stream().map(ChatLogDTO::fromEntity).toList();
    }
}
