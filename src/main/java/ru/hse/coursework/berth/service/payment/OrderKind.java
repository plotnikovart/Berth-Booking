package ru.hse.coursework.berth.service.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum OrderKind implements EnumWithIdentifier<Integer> {

    BOOKING_SERVICE_FEE(0);

    private final Integer identifier;

    public static class Converter extends AbstractEnumConverter<OrderKind, Integer> {
    }
}
