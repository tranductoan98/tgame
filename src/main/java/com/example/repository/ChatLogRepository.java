package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.ChatLog;

public interface ChatLogRepository extends JpaRepository<ChatLog, Integer> {
    List<ChatLog> findByMap_Mapid(int mapId);
    List<ChatLog> findByReceiver_IdAndSender_Id(int receiverId, int senderId);
    List<ChatLog> findTop100ByOrderBySentAtDesc();
}