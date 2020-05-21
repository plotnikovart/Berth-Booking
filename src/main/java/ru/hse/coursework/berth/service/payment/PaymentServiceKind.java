package ru.hse.coursework.berth.service.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@RequiredArgsConstructor
public enum PaymentServiceKind implements EnumWithIdentifier<Integer> {

    TINKOFF(0);

    private final Integer identifier;

    public static class Converter extends AbstractEnumConverter<PaymentServiceKind, Integer> {
    }
}
