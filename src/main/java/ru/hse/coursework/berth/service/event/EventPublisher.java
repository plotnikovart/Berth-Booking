package ru.hse.coursework.berth.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.service.event.booking.CancelBookingEvent;
import ru.hse.coursework.berth.service.event.booking.OpenBookingEvent;

import static ru.hse.coursework.berth.common.TransactionHelper.actionAfterTransaction;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void reviewPublish(Long berthId, Long reviewId) {
        var event = new ReviewPublishEvent(this)
                .setBerthId(berthId)
                .setReviewId(reviewId);
        publishEvent(event);
    }

    public void reviewDelete(Long berthId, Long reviewId) {
        var event = new ReviewDeleteEvent(this)
                .setBerthId(berthId)
                .setReviewId(reviewId);
        publishEvent(event);
    }

    public void openBooking(long bookingId) {
        var event = new OpenBookingEvent(this)
                .setBookingId(bookingId);
        publishEvent(event);
    }

    public void cancelBooking(long bookingId) {
        var event = new CancelBookingEvent(this)
                .setBookingId(bookingId);
        publishEvent(event);
    }

    private void publishEvent(Object event) {
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }
}
