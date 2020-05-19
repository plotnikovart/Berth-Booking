package ru.hse.coursework.berth.service.booking.fsm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.fsm.EventEnum;

@Getter
@AllArgsConstructor
public enum BookingEvent implements EventEnum {

    REJECT("Dockmaster reject booking"),
    APPROVE("Dockmaster approve booking"),
    PAY("Boater pay booking"),
    CANCEL("Boater cancel booking");

    private final String description;
}
