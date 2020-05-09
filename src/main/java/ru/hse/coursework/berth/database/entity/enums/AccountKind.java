package ru.hse.coursework.berth.database.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum AccountKind implements EnumWithIdentifier<Integer> {

    EMAIL(0),
    GOOGLE(1),
    FACEBOOK(2);

    private final Integer identifier;

    public static class Converter extends AbstractEnumConverter<AccountKind, Integer> {
    }
}
