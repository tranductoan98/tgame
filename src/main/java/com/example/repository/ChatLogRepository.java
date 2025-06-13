package com.example.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.ChatLog;
import com.example.enums.ChatChannel;

public interface ChatLogRepository extends JpaRepository<ChatLog, Integer> {

    List<ChatLog> findByMap_Id(int mapId);

    Page<ChatLog> findByMap_IdOrderBySentatDesc(int mapId, Pageable pageable);

    Page<ChatLog> findBySender_PlayeridAndReceiver_PlayeridOrderBySentatDesc(int senderId, int receiverId, Pageable pageable);

    @Query("""
        SELECT c FROM ChatLog c 
        WHERE (c.sender.id = :player1Id AND c.receiver.id = :player2Id)
           OR (c.sender.id = :player2Id AND c.receiver.id = :player1Id)
        ORDER BY c.sentat DESC
    """)
    Page<ChatLog> findPrivateChatBetween(int player1Id, int player2Id, Pageable pageable);
    Page<ChatLog> findByChannelOrderBySentatDesc(ChatChannel channel, Pageable pageable);
}
