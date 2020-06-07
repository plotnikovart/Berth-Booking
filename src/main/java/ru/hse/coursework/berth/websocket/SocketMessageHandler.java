package ru.hse.coursework.berth.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.chat.ChatFacade;
import ru.hse.coursework.berth.websocket.event.IncomingEventEnum;
import ru.hse.coursework.berth.websocket.event.IncomingMessage;
import ru.hse.coursework.berth.websocket.event.incoming.ChatMessageIncomingDto;
import ru.hse.coursework.berth.websocket.event.incoming.ChatOffsetIncomingDto;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SocketMessageHandler {

    private final ObjectMapper mapper;
    private final ChatFacade chatFacade;

    public void handleMessage(String message) {
        switch (detectEvent(message)) {
            case CHAT_OFFSET: {
                var body = parseBody(message, ChatOffsetIncomingDto.class);
                chatFacade.updateAccountChatOffset(body.getData().getChatId(), body.getData().getOffset());
                break;
            }
            case CHAT_MESSAGE: {
                var body = parseBody(message, ChatMessageIncomingDto.class);
                chatFacade.sendMessage(body.getData().getChatId(), body.getData().getMessage());
                break;
            }
        }
    }


    private IncomingEventEnum detectEvent(String message) {
        return parseBody(message, IncomingMessage.class).getEvent();
    }

    private <T> T parseBody(String message, Class<T> clazz) {
        try {
            T body = mapper.readValue(message, clazz);
            ValidationUtils.validateEntity(body);
            return body;
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
}
