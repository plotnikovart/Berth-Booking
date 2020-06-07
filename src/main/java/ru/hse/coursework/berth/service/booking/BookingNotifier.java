package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.email.EmailService;
import ru.hse.coursework.berth.service.event.booking.*;

@Component
@RequiredArgsConstructor
public class BookingNotifier {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    @EventListener
    @Transactional
    public void onBookingOpen(OpenBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);
//        emailService.sendBookingOpen(booking);
    }

    @EventListener
    @Transactional
    public void onBookingApprove(ApproveBookingEvent event) {

    }

    @EventListener
    @Transactional
    public void onBookingReject(RejectBookingEvent event) {

    }

    @EventListener
    @Transactional
    public void onBookingCancel(CancelBookingEvent event) {

    }

    @EventListener
    public void onBookingPay(PayBookingEvent event) {

    }
}
