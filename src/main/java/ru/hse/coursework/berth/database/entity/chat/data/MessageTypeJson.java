package ru.hse.coursework.berth.database.entity.chat.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.hse.coursework.berth.common.enums.EnumHelper;
import ru.hse.coursework.berth.database.entity.chat.enums.MessageType;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageTypeJson {

    public static class Serializer extends JsonSerializer<MessageType> {

        @Override
        public void serialize(MessageType messageType,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeObject(messageType.getIdentifier());
        }
    }

    public static class Deserializer extends JsonDeserializer<MessageType> {

        @Override
        public MessageType deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Integer value = (Integer) jsonParser.getCurrentValue();
            return EnumHelper.getEnumByIdentifier(value, MessageType.class);
        }
    }
}
