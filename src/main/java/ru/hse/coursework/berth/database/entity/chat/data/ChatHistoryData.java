package ru.hse.coursework.berth.database.entity.chat.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.hse.coursework.berth.database.entity.chat.enums.MessageType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatHistoryData {

    @JsonProperty("m")
    private List<MessageDto> messages;

    public static class MessageDto {

        @JsonProperty("s")
        private Long senderId;

        @JsonProperty("o")
        private Long offset;

        @JsonProperty("dt")
        private LocalDateTime sendDateTime;

        @JsonProperty("type")
        @JsonSerialize(using = MessageTypeJson.Serializer.class)
        @JsonDeserialize(using = MessageTypeJson.Deserializer.class)
        private MessageType messageType;

        @JsonProperty("text")
        private String messageText;
    }
}
