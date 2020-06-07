package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.account.AccountService;
import ru.hse.coursework.berth.service.email.EmailService;
import ru.hse.coursework.berth.service.email.dto.BookingInfo;
import ru.hse.coursework.berth.service.event.booking.*;

@Component
@RequiredArgsConstructor
public class BookingNotifier {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final AccountService accountService;

    @Async
    @EventListener
    @Transactional
    public void onBookingOpen(OpenBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        String email = accountService.getAccountEmail(booking.getBerthPlace().getBerth().getOwner());

        if (email == null) {
            return;
        }
        emailService.sendBookingOpen(email, bookingInfo);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingApprove(ApproveBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        String email = accountService.getAccountEmail(booking.getRenter());

        if (email == null) {
            return;
        }
        emailService.sendBookingApprove(email, bookingInfo);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingReject(RejectBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        String email = accountService.getAccountEmail(booking.getRenter());

        if (email == null) {
            return;
        }
        emailService.sendBookingReject(email, bookingInfo);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingPay(PayBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        String email = accountService.getAccountEmail(booking.getBerthPlace().getBerth().getOwner());

        if (email == null) {
            return;
        }
        emailService.sendBookingPay(email, bookingInfo);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingCancel(CancelBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        String email = accountService.getAccountEmail(booking.getBerthPlace().getBerth().getOwner());

        if (email == null) {
            return;
        }
        emailService.sendBookingCancel(email, bookingInfo);
    }

    private BookingInfo formBookingInfo(Booking booking) {
        return new BookingInfo()
                .setBerthName(booking.getBerthPlace().getBerth().getName())
                .setPlaceName(booking.getBerthPlace().getName())
                .setFromDate(booking.getStartDate())
                .setToDate(booking.getEndDate())
                .setOwner(accountService.getAccountFullName(booking.getBerthPlace().getBerth().getOwner()))
                .setRenter(accountService.getAccountFullName(booking.getRenter()))
                .setPrice(booking.getTotalPrice())
                .setServiceFee(booking.getServiceFee());
    }
}
