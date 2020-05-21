package ru.hse.coursework.berth.service.booking.fsm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.fsm.EventEnum;

@Getter
@AllArgsConstructor
public enum BookingEvent implements EventEnum {

    REJECT("Dockmaster rejects booking"),
    APPROVE("Dockmaster approves booking"),
    PAY_PREPARE("Boater prepares to pay for booking"),
    PAY("Boater pays booking"),
    CANCEL("Boater cancels booking"),

    OTHER_PAY("Other booking was payed");

    private final String description;
}
