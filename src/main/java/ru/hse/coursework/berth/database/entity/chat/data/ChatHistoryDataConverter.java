package ru.hse.coursework.berth.database.entity.chat.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

@Component
@RequiredArgsConstructor
public class ChatHistoryDataConverter implements AttributeConverter<ChatHistoryData, String> {

    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(ChatHistoryData d) {
        return mapper.writeValueAsString(d);
    }

    @Override
    @SneakyThrows
    public ChatHistoryData convertToEntityAttribute(String dbData) {
        return mapper.readValue(dbData, ChatHistoryData.class);
    }
}
