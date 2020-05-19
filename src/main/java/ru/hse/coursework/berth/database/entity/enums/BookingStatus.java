package ru.hse.coursework.berth.database.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;
import ru.hse.coursework.berth.common.fsm.StateEnum;

@Getter
@AllArgsConstructor
public enum BookingStatus implements EnumWithIdentifier<String>, StateEnum {

    NEW("N", "New Booking"),
    REJECTED("R", "Booking is rejected by dockmaster"),
    APPROVED("A", "Booking is approved by dockmaster"),
    PAYED("P", "Booking is payed by boater"),
    CANCELLED("C", "Booking is cancelled by boater");

    private final String identifier;
    private final String description;

    public static class Converter extends AbstractEnumConverter<BookingStatus, String> {
    }
}
