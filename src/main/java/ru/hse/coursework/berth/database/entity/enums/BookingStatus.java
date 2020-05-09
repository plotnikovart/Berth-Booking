package ru.hse.coursework.berth.database.entity.enums;


import lombok.AllArgsConstructor;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@AllArgsConstructor
public enum BookingStatus implements EnumWithIdentifier<String> {

    NEW("N"),
    REJECTED("R"),
    APPROVED("A"),
    CANCELLED("C");

    private String code;

    @Override
    public String getIdentifier() {
        return code;
    }

    public static class Converter extends AbstractEnumConverter<BookingStatus, String> {
    }
}
