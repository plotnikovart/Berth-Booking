package ru.hse.coursework.berth.service.event.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CancelBookingEvent extends ApplicationEvent {

    private Long bookingId;

    public CancelBookingEvent(Object source) {
        super(source);
    }
}
