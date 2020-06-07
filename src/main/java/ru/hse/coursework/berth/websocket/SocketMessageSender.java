package ru.hse.coursework.berth.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.hse.coursework.berth.websocket.event.OutgoingMessage;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SocketMessageSender {

    private final ObjectMapper mapper;
    private final SocketSessionManager socketSessionManager;

    @SneakyThrows
    public <T extends OutgoingMessage<?>> void sendMessage(Long accountId, T message) {
        String json = mapper.writeValueAsString(message);

        Set<WebSocketSession> sessions = socketSessionManager.getSessionsByAccount(accountId);
        for (var session : sessions) {
            session.sendMessage(new TextMessage(json));
        }
    }
}
