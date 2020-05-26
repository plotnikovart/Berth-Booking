package ru.hse.coursework.berth.web.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.chat.ChatFacade;
import ru.hse.coursework.berth.service.websocket.SocketSessionManager;
import ru.hse.coursework.berth.web.socket.dto.ChatMessageSocketDto;
import ru.hse.coursework.berth.web.socket.dto.ChatOffsetSocketDto;
import ru.hse.coursework.berth.web.socket.dto.SocketMessage;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final SocketSessionManager socketSessionManager;
    private final ChatFacade chatFacade;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Long accountId = socketSessionManager.extractAccountId(session);
            OperationContext.accountId(accountId);

            switch (detectEvent(message)) {
                case CHAT_OFFSET: {
                    var body = parseBody(message, ChatOffsetSocketDto.class);
                    chatFacade.updateAccountChatOffset(body.getData().getChatId(), body.getData().getOffset());
                    break;
                }
                case CHAT_MESSAGE: {
                    var body = parseBody(message, ChatMessageSocketDto.class);
                    chatFacade.sendMessage(body.getData().getChatId(), body.getData().getMessage());
                    break;
                }
            }
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


    private SocketEvent detectEvent(TextMessage message) {
        return parseBody(message, SocketMessage.class).getEvent();
    }

    private <T> T parseBody(TextMessage message, Class<T> clazz) {
        try {
            T body = mapper.readValue(message.getPayload(), clazz);
            ValidationUtils.validateEntity(body);
            return body;
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
}

