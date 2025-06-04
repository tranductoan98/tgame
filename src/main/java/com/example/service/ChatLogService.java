package com.example.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import com.example.entity.ChatLog;
import com.example.enums.ChatChannel;

public interface ChatLogService {
    ChatLog sendMessage(ChatLog chatLog);
    Page<ChatLog> getChatByMap(int mapId, Pageable pageable);
    Page<ChatLog> getPrivateChat(int player1Id, int player2Id, Pageable pageable);
    List<ChatLog> getLatestChatInMap(int mapId, int limit);
    Page<ChatLog> getOneWayPrivateChat(int senderId, int receiverId, Pageable pageable);
    Page<ChatLog> getChatByChannel(ChatChannel channel, Pageable pageable);
}
