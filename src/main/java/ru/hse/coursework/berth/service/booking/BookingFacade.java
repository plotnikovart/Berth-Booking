package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.booking.dto.BookingPayLinkResp;
import ru.hse.coursework.berth.service.booking.dto.BookingStatusResp;
import ru.hse.coursework.berth.service.event.EventPublisher;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final RenterBookingService renterBookingService;
    private final OwnerBookingService ownerBookingService;
    private final BookingPaymentService paymentService;
    private final EventPublisher eventPublisher;

    public List<BookingDto.RespRenter> getMyBookings() {
        return renterBookingService.getBookings();
    }

    public BookingDto.RespRenter openBooking(BookingDto.Req bookingRequest) {
        long bookingId = renterBookingService.openBooking(bookingRequest);
        eventPublisher.openBooking(bookingId);
        return renterBookingService.getBooking(bookingId);
    }

    public BookingPayLinkResp payBooking(Long bookingId) {
        return paymentService.formPaymentLink(bookingId);
    }

    public BookingStatusResp cancelBooking(Long bookingId) {
        BookingStatus status = renterBookingService.cancelBooking(bookingId);
        eventPublisher.cancelBooking(bookingId);
        return BookingStatusResp.of(status);
    }


    public List<BookingDto.RespOwner> getBookingsForBerth(Long berthId) {
        return ownerBookingService.getBookingsForBerth(berthId);
    }


    public BookingStatusResp approveBooking(Long bookingId) {
        BookingStatus status = ownerBookingService.approveBooking(bookingId);
        eventPublisher.approveBooking(bookingId);
        return BookingStatusResp.of(status);
    }

    public BookingStatusResp rejectBooking(Long bookingId) {
        BookingStatus status = ownerBookingService.rejectBooking(bookingId);
        eventPublisher.rejectBooking(bookingId);
        return BookingStatusResp.of(status);
    }
}
