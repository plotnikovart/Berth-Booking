package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.common.DateHelper;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.converters.impl.RenterBookingConverter;
import ru.hse.coursework.berth.service.event.EventPublisher;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerBookingService {

    private final BookingRepository bookingRepository;
    private final UserInfoRepository userInfoRepository;
    private final BerthRepository berthRepository;
    private final BookingSearchService bookingSearchService;
    private final PermissionService permissionService;
    private final RenterBookingConverter bookingConverter;
    private final RenterBookingService renterBookingService;
    private final EventPublisher eventPublisher;

    public List getBookingsForBerth(long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.check(berth);

        List<Booking> bookings = bookingRepository.findAllByBerth(berth);
        return bookingConverter.toDtos(bookings);
    }

    public void approveBooking(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
//
//        if (booking.getOwner() != userInfoRepository.findCurrent()) {
//            throw new AccessException();
//        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ServiceException(SMessageSource.message("booking.is_cancelled"));
        }

        // Для других бронирований на пересекающиеся даты устанавливаем статус - отклонено
        booking.getBerthPlace().getBookingList().stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .filter(b -> DateHelper.isIntersect(b.getStartDate(), b.getEndDate(), booking.getStartDate(), booking.getEndDate()))
                .forEach(b -> b.setStatus(BookingStatus.REJECTED));

        booking.setStatus(BookingStatus.APPROVED);
//        emailService.sendBookingApprove(booking);
    }

    public void rejectBooking(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
//
//        if (booking.getOwner() != userInfoRepository.findCurrent()) {
//            throw new AccessException();
//        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ServiceException(SMessageSource.message("booking.is_cancelled"));
        }

        booking.setStatus(BookingStatus.REJECTED);
    }
}
