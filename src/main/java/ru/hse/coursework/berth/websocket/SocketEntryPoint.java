package ru.hse.coursework.berth.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.hse.coursework.berth.config.security.OperationContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketEntryPoint extends TextWebSocketHandler {

    private final SocketMessageHandler messageHandler;
    private final SocketSessionManager socketSessionManager;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Long accountId = socketSessionManager.extractAccountId(session);
            OperationContext.accountId(accountId);

            messageHandler.handleMessage(message.getPayload());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            OperationContext.clear();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        socketSessionManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        socketSessionManager.removeSession(session);
    }
}

