package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.booking.dto.BookingStatusResp;
import ru.hse.coursework.berth.service.converters.impl.RenterBookingConverter;
import ru.hse.coursework.berth.service.event.EventPublisher;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final BookingRepository bookingRepository;
    private final BerthRepository berthRepository;
    private final BookingSearchService bookingSearchService;
    private final PermissionService permissionService;
    private final RenterBookingConverter bookingConverter;
    private final RenterBookingService renterBookingService;
    private final EventPublisher eventPublisher;

    public BookingDto.RespRenter openBooking(BookingDto.Req bookingRequest) {
        long bookingId = renterBookingService.openBooking(bookingRequest);
        eventPublisher.openBooking(bookingId);
        return renterBookingService.getBooking(bookingId);
    }

    public List<BookingDto.RespRenter> getMyBookings() {
        return renterBookingService.getBookings();
    }


    public BookingStatusResp cancelBooking(Long bookingId) {
        BookingStatus status = renterBookingService.cancelBooking(bookingId);
        eventPublisher.cancelBooking(bookingId);
        return BookingStatusResp.of(status);
    }

    public List<BookingDto.RespRenter> getBookingsForBerth(Long berthId) {

    }


    public BookingStatusResp approveBooking(Long bookingId) {
        return BookingStatusResp.of(null);
    }

    public BookingStatusResp rejectBooking(Long bookingId) {
        return BookingStatusResp.of(null);
    }
}
