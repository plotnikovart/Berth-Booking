package ru.hse.coursework.berth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.hse.coursework.berth.config.security.SocketHandshakeHandler;
import ru.hse.coursework.berth.web.socket.SocketHandler;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;
    private final SocketHandshakeHandler socketHandshakeHandler;


    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(socketHandler, "/socket")
                .setAllowedOrigins("*")
                .setHandshakeHandler(socketHandshakeHandler);
    }
}
