package com.example.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.handler.ChatWebSocketHandler;
import com.example.handler.GameWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameWebSocketHandler gameWebSocketHandler;
    private final ChatWebSocketHandler chatWebSocketHandler;

    public WebSocketConfig(GameWebSocketHandler gameWebSocketHandler, ChatWebSocketHandler chatWebSocketHandler) {
        this.gameWebSocketHandler = gameWebSocketHandler;
		this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler(gameWebSocketHandler, "/ws-game")
            .setAllowedOrigins("*");

        registry
        	.addHandler(chatWebSocketHandler, "/ws-chat")
        	.setAllowedOrigins("*");
    }
}

