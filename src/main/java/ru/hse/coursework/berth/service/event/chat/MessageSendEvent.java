package ru.hse.coursework.berth.service.event.chat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;

@Getter
@Setter
public class MessageSendEvent extends ApplicationEvent {

    private Long chatId;
    private MessageDto.Resp messageDto;

    public MessageSendEvent(Object source) {
        super(source);
    }
}
