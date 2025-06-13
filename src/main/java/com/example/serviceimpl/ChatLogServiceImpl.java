package com.example.serviceimpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.entity.ChatLog;
import com.example.enums.ChatChannel;
import com.example.repository.ChatLogRepository;
import com.example.service.ChatLogService;
import com.example.service.PlayerPositionService;

@Service
public class ChatLogServiceImpl implements ChatLogService{

	private final ChatLogRepository chatLogRepository;
	private final PlayerPositionService playerPositionService;
	
	public ChatLogServiceImpl(ChatLogRepository chatLogRepository, PlayerPositionService playerPositionService) {
		this.chatLogRepository = chatLogRepository;
		this.playerPositionService = playerPositionService;
	}

	@Override
	public ChatLog sendMessage(ChatLog chatLog) {
		chatLog.setSentat(java.time.LocalDateTime.now());
        return chatLogRepository.save(chatLog);
	}

	@Override
	public Page<ChatLog> getChatByMap(int mapId, Pageable pageable) {
		int currentUserId = getCurrentUserId();
		
		if (!playerPositionService.isPlayerInMap(currentUserId, mapId)) {
	        throw new AccessDeniedException("Không có quyền xem chat map này");
	    }
		
		return chatLogRepository.findByMap_IdOrderBySentatDesc(mapId, pageable);
	}

	@Override
	public Page<ChatLog> getPrivateChat(int player1Id, int player2Id, Pageable pageable) {
		int currentUserId = getCurrentUserId();
		
		if (currentUserId != player1Id && currentUserId != player2Id) {
	        throw new AccessDeniedException("Không có quyền xem chat này");
	    }
		return chatLogRepository.findPrivateChatBetween(player1Id, player2Id, pageable);
	}

	@Override
	public List<ChatLog> getLatestChatInMap(int mapId, int limit) {
		Pageable pageable = PageRequest.of(0, limit);
        return chatLogRepository.findByMap_IdOrderBySentatDesc(mapId, pageable).getContent();
	}

	@Override
	public Page<ChatLog> getOneWayPrivateChat(int senderId, int receiverId, Pageable pageable) {
		return chatLogRepository.findBySender_PlayeridAndReceiver_PlayeridOrderBySentatDesc(senderId, receiverId, pageable);
	}

	@Override
	public Page<ChatLog> getChatByChannel(ChatChannel channel, Pageable pageable) {
		if (channel == ChatChannel.PRIVATE) {
	        throw new AccessDeniedException("Vui lòng dùng API private chat riêng");
	    }
		
		return chatLogRepository.findByChannelOrderBySentatDesc(channel, pageable);
	}

	private int getCurrentUserId() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    return Integer.parseInt(auth.getName());
	}
	
}
