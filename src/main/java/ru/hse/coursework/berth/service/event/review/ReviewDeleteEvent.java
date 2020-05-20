package ru.hse.coursework.berth.service.event.review;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ReviewDeleteEvent extends ApplicationEvent {

    private Long berthId;
    private Long reviewId;

    public ReviewDeleteEvent(Object source) {
        super(source);
    }
}
