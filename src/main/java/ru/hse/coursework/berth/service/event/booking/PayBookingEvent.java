package ru.hse.coursework.berth.service.event.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class PayBookingEvent extends ApplicationEvent {

    private Long bookingId;

    public PayBookingEvent(Object source) {
        super(source);
    }
}
