package ru.hse.coursework.berth.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.service.event.booking.*;
import ru.hse.coursework.berth.service.event.review.ReviewDeleteEvent;
import ru.hse.coursework.berth.service.event.review.ReviewPublishEvent;

import static ru.hse.coursework.berth.common.TransactionHelper.actionAfterTransaction;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void reviewPublish(Long berthId, Long reviewId) {
        var event = new ReviewPublishEvent(this)
                .setBerthId(berthId)
                .setReviewId(reviewId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }

    public void reviewDelete(Long berthId, Long reviewId) {
        var event = new ReviewDeleteEvent(this)
                .setBerthId(berthId)
                .setReviewId(reviewId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }

    public void openBooking(long bookingId) {
        var event = new OpenBookingEvent(this)
                .setBookingId(bookingId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }

    public void cancelBooking(long bookingId) {
        var event = new CancelBookingEvent(this)
                .setBookingId(bookingId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }

    public void approveBooking(long bookingId) {
        var event = new ApproveBookingEvent(this)
                .setBookingId(bookingId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }

    public void rejectBooking(long bookingId) {
        var event = new RejectBookingEvent(this)
                .setBookingId(bookingId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }

    public void payBooking(long bookingId) {
        var event = new PayedBookingEvent(this)
                .setBookingId(bookingId);
        actionAfterTransaction(() -> eventPublisher.publishEvent(event));
    }
}
