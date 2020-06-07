package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.account.AccountService;
import ru.hse.coursework.berth.service.email.EmailService;
import ru.hse.coursework.berth.service.email.dto.BookingInfo;
import ru.hse.coursework.berth.service.event.booking.*;
import ru.hse.coursework.berth.websocket.SocketMessageSender;
import ru.hse.coursework.berth.websocket.event.outgoing.*;

@Component
@RequiredArgsConstructor
public class BookingNotifier {

    private final BookingRepository bookingRepository;
    private final EmailService emailService;
    private final AccountService accountService;
    private final SocketMessageSender socketMessageSender;

    @Async
    @EventListener
    @Transactional
    public void onBookingOpen(OpenBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        Account receiver = booking.getBerthPlace().getBerth().getOwner();

        String email = accountService.getAccountEmail(receiver);
        if (email != null) {
            emailService.sendBookingOpen(email, bookingInfo);
        }

        var message = new BookingOpenOutgoingDto(
                new BookingOpenOutgoingDto.D()
                        .setBookingId(event.getBookingId())
        );
        socketMessageSender.sendMessage(receiver.getId(), message);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingApprove(ApproveBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        Account receiver = booking.getRenter();

        String email = accountService.getAccountEmail(receiver);

        if (email != null) {
            emailService.sendBookingApprove(email, bookingInfo);
        }

        var message = new BookingApproveOutgoingDto(
                new BookingApproveOutgoingDto.D()
                        .setBookingId(event.getBookingId())
        );
        socketMessageSender.sendMessage(receiver.getId(), message);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingReject(RejectBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        Account receiver = booking.getRenter();

        String email = accountService.getAccountEmail(receiver);
        if (email != null) {
            emailService.sendBookingReject(email, bookingInfo);
        }

        var message = new BookingRejectOutgoingDto(
                new BookingRejectOutgoingDto.D()
                        .setBookingId(event.getBookingId())
        );
        socketMessageSender.sendMessage(receiver.getId(), message);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingPay(PayBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        Account receiver = booking.getBerthPlace().getBerth().getOwner();

        String email = accountService.getAccountEmail(receiver);
        if (email != null) {
            emailService.sendBookingPay(email, bookingInfo);
        }

        var message = new BookingPayOutgoingDto(
                new BookingPayOutgoingDto.D()
                        .setBookingId(event.getBookingId())
        );
        socketMessageSender.sendMessage(receiver.getId(), message);
    }

    @Async
    @EventListener
    @Transactional
    public void onBookingCancel(CancelBookingEvent event) {
        Booking booking = bookingRepository.findById(event.getBookingId()).orElseThrow(NotFoundException::new);

        BookingInfo bookingInfo = formBookingInfo(booking);
        Account receiver = booking.getBerthPlace().getBerth().getOwner();

        String email = accountService.getAccountEmail(receiver);
        if (email != null) {
            emailService.sendBookingCancel(email, bookingInfo);
        }

        var message = new BookingCancelOutgoingDto(
                new BookingCancelOutgoingDto.D()
                        .setBookingId(event.getBookingId())
        );
        socketMessageSender.sendMessage(receiver.getId(), message);
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
