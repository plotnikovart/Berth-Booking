package ru.hse.coursework.berth.service.event.booking;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ApproveBookingEvent extends ApplicationEvent {

    private Long bookingId;

    public ApproveBookingEvent(Object source) {
        super(source);
    }
}
