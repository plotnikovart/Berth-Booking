package ru.hse.coursework.berth.service.event.review;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class ReviewPublishEvent extends ApplicationEvent {

    private Long berthId;
    private Long reviewId;

    public ReviewPublishEvent(Object source) {
        super(source);
    }
}
