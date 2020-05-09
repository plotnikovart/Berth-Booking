package ru.hse.coursework.berth.database.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum AccountRole implements EnumWithIdentifier<Integer> {

    USER(0),
    MODERATOR(1),
    ADMIN(2);

    private Integer identifier;

    public static class Converter extends AbstractEnumConverter<AccountRole, Integer> {
    }
}
