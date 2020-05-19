package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.*;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.booking.fsm.BookingEvent;
import ru.hse.coursework.berth.service.booking.fsm.BookingFSMHandler;
import ru.hse.coursework.berth.service.converters.impl.RenterBookingConverter;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class RenterBookingService {

    private final BookingRepository bookingRepository;
    private final UserInfoRepository userInfoRepository;
    private final BerthRepository berthRepository;
    private final BerthPlaceRepository berthPlaceRepository;
    private final BookingSearchService bookingSearchService;
    private final PermissionService permissionService;
    private final RenterBookingConverter converter;
    private final AccountRepository accountRepository;
    private final BookingFSMHandler bookingFSMHandler;

    @Transactional
    public Long openBooking(BookingDto.Req bookingRequest) {
        Account renter = accountRepository.findCurrent();

        Booking booking = converter.toEntity(bookingRequest);
        booking
                .setTotalPrice(calcTotalPrice(booking))
                .setRenter(renter);

        validateBooking(booking);

        return bookingRepository.save(booking).getId();
    }

    public BookingDto.RespRenter getBooking(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(NotFoundException::new);
        checkAccess(booking);

        return converter.toDto(booking);
    }

    public List<BookingDto.RespRenter> getBookings() {
        Account account = accountRepository.findCurrent();
        List<Booking> bookings = bookingRepository.findAllByRenterLoadBerthWithAmenitiesAndShip(account);
        return converter.toDtos(bookings);
    }


    @Transactional
    public BookingStatus cancelBooking(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        checkAccess(booking);

        bookingFSMHandler.sendEvent(booking, BookingEvent.CANCEL);
        return booking.getStatus();
    }


    private void checkAccess(Booking booking) {
        if (!booking.getRenter().getId().equals(OperationContext.accountId())) {
            throw new AccessException();
        }
    }

    private void validateBooking(Booking booking) {
        if (booking.getShip().getOwner() != booking.getRenter()) {
            throw new AccessException();
        }

        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            throw new ServiceException(SMessageSource.message("booking.date"));
        }

        if (!bookingSearchService.isMatch(booking.getBerthPlace(), booking.getShip())) {
            throw new ServiceException(SMessageSource.message("booking.size"));
        }

        if (bookingSearchService.isReserved(booking.getBerthPlace(), booking.getStartDate(), booking.getEndDate())) {
            throw new ServiceException(SMessageSource.message("booking.is_reserved"));
        }
    }

    private Double calcTotalPrice(Booking booking) {
        Double price = booking.getBerthPlace().getPrice();
        return price * DAYS.between(booking.getStartDate(), booking.getEndDate());
    }
}
