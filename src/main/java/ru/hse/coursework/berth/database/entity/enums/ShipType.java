package ru.hse.coursework.berth.database.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum ShipType implements EnumWithIdentifier<Integer> {

    POWER(0),
    SAIL(1);

    private final Integer identifier;

    public static class Converter extends AbstractEnumConverter<ShipType, Integer> {
    }
}
