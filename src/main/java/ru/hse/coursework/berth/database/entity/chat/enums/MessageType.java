package ru.hse.coursework.berth.database.entity.chat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum MessageType implements EnumWithIdentifier<Integer> {

    TEXT(0);

    private final Integer identifier;

    public static class Converter extends AbstractEnumConverter<MessageType, Integer> {
    }
}
