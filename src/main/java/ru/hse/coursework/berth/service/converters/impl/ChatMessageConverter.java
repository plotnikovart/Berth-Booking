package ru.hse.coursework.berth.service.converters.impl;

import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.chat.ChatMessage;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.service.converters.AbstractRespConverter;

@Component
public class ChatMessageConverter extends AbstractRespConverter<ChatMessage, MessageDto.Resp> {

    @Override
    public MessageDto.Resp toDto(MessageDto.Resp dto, ChatMessage message) {
        return (MessageDto.Resp) dto
                .setId(message.getId())
                .setOffset(message.getOffset())
                .setSendDateTime(message.getSendDateTime())
                .setParticipantId(message.getSender().getId())
                .setText(message.getMessageText())
                .setType(message.getMessageType());
    }
}
