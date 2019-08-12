package app.service.facade;

import app.common.DateHelper;
import app.common.SMessageSource;
import app.common.exception.AccessException;
import app.common.exception.NotFoundException;
import app.common.exception.ServiceException;
import app.database.entity.*;
import app.database.entity.enums.BookingStatus;
import app.database.repository.*;
import app.service.BookingSearchService;
import app.service.PermissionService;
import app.web.dto.BookingDto;
import app.web.dto.BookingReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final BookingRepository bookingRepository;
    private final UserInfoRepository userInfoRepository;
    private final ShipRepository shipRepository;
    private final BerthRepository berthRepository;
    private final BerthPlaceRepository berthPlaceRepository;
    private final BookingSearchService bookingSearchService;
    private final PermissionService permissionService;

    @Transactional
    public synchronized Long createBooking(BookingReqDto bookingReqDto) {
        BerthPlace berthPlace = berthPlaceRepository.findById(bookingReqDto.getBerthPlaceId()).orElseThrow(NotFoundException::new);
        Ship ship = shipRepository.findById(bookingReqDto.getShipId()).orElseThrow(NotFoundException::new);

        var startDate = DateHelper.convertToLocalDate(bookingReqDto.getStartDate());
        var endDate = DateHelper.convertToLocalDate(bookingReqDto.getEndDate());

        UserInfo owner = userInfoRepository.findById(berthPlace.getOwnerId()).orElseThrow(NotFoundException::new);
        UserInfo renter = userInfoRepository.findCurrent();

        if (renter != ship.getUserInfo()) {
            throw new AccessException();
        }

        var booking = new Booking()
                .setBerthPlace(berthPlace)
                .setShip(ship)
                .setOwner(owner)
                .setRenter(renter)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setStatus(BookingStatus.NEW);

        validateBooking(booking);
        return bookingRepository.save(booking).getId();
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);

        if (booking.getRenter() != userInfoRepository.findCurrent()) {
            throw new AccessException();
        }

        booking.setStatus(BookingStatus.CANCELLED);
    }

    @Transactional
    public synchronized void approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);

        if (booking.getOwner() != userInfoRepository.findCurrent()) {
            throw new AccessException();
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ServiceException(SMessageSource.get("booking.is_cancelled"));
        }

        // Для других бронирований на пересекающиеся даты устанавливаем статус - отклонено
        booking.getBerthPlace().getBookingList().stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .filter(b -> DateHelper.isIntersect(b.getStartDate(), b.getEndDate(), booking.getStartDate(), booking.getEndDate()))
                .forEach(b -> b.setStatus(BookingStatus.REJECTED));

        booking.setStatus(BookingStatus.APPROVED);
    }

    @Transactional
    public void rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);

        if (booking.getOwner() != userInfoRepository.findCurrent()) {
            throw new AccessException();
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ServiceException(SMessageSource.get("booking.is_cancelled"));
        }

        booking.setStatus(BookingStatus.REJECTED);
    }

    @Transactional(readOnly = true)
    public BookingDto.WithId getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);

        UserInfo current = userInfoRepository.findCurrent();
        if (booking.getOwner() == current || booking.getRenter() == current) {
            return booking.getDto();
        }

        throw new AccessException();
    }

    @Transactional(readOnly = true)
    public List<BookingDto.WithId> getAllBookings() {
        UserInfo userInfo = userInfoRepository.findCurrent();
        return userInfo.getBookings().stream()
                .map(Booking::getDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDto.WithId> getAllBookingsByBerth(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.checkPermission(berth);

        return bookingRepository.findAllByBerth(berth).stream()
                .map(Booking::getDto)
                .collect(Collectors.toList());
    }

    private void validateBooking(Booking booking) {
        if (booking.getEndDate().isBefore(booking.getStartDate())) {
            throw new ServiceException(SMessageSource.get("booking.date"));
        }

        var place = booking.getBerthPlace();
        var ship = booking.getShip();

        if (!(ship.getLength() <= place.getLength() && ship.getWidth() <= place.getWidth() && ship.getDraft() <= place.getDraft())) {
            throw new ServiceException(SMessageSource.get("booking.size"));
        }

        if (bookingSearchService.isReserved(booking.getBerthPlace(), booking.getStartDate(), booking.getEndDate())) {
            throw new ServiceException(SMessageSource.get("booking.is_reserved"));
        }
    }
}
