package app.service.facade;

import app.common.DateHelper;
import app.common.SMessageSource;
import app.common.exception.AccessException;
import app.common.exception.NotFoundException;
import app.common.exception.ServiceException;
import app.database.entity.Berth;
import app.database.entity.Booking;
import app.database.entity.UserInfo;
import app.database.entity.enums.BookingStatus;
import app.database.repository.BerthPlaceRepository;
import app.database.repository.BerthRepository;
import app.database.repository.BookingRepository;
import app.database.repository.UserInfoRepository;
import app.service.BookingSearchService;
import app.service.EmailService;
import app.service.PermissionService;
import app.service.converters.impl.BookingConverter;
import app.web.dto.BookingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@RequiredArgsConstructor
public class BookingFacade {

    private final BookingRepository bookingRepository;
    private final UserInfoRepository userInfoRepository;
    private final BerthRepository berthRepository;
    private final BerthPlaceRepository berthPlaceRepository;
    private final BookingSearchService bookingSearchService;
    private final PermissionService permissionService;
    private final EmailService emailService;
    private final BookingConverter bookingConverter;

    @Transactional
    public synchronized Long createBooking(BookingDto.Req bookingRequest) {
        UserInfo owner = berthPlaceRepository.findById(bookingRequest.getBerthPlaceId()).orElseThrow(NotFoundException::new)
                .getBerth().getUserInfo();
        UserInfo renter = userInfoRepository.findCurrent();

        var booking = bookingConverter.toEntity(bookingRequest)
                .setOwner(owner)
                .setRenter(renter)
                .setStatus(BookingStatus.NEW);

        validateBooking(booking);
        Long id = bookingRepository.save(booking).getId();
        emailService.sendBookingCreate(booking);
        return id;
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
        emailService.sendBookingApprove(booking);
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
    public BookingDto.Resp getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);

        UserInfo current = userInfoRepository.findCurrent();
        if (booking.getOwner() == current || booking.getRenter() == current) {
            return bookingConverter.toDto(booking);
        }

        throw new AccessException();
    }

    @Transactional(readOnly = true)
    public List<BookingDto.Resp> getAllBookings() {
        UserInfo userInfo = userInfoRepository.findCurrent();
        return bookingConverter.toDtos(userInfo.getBookings());
    }

    @Transactional(readOnly = true)
    public List<BookingDto.Resp> getAllBookingsByBerth(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.check(berth);

        List<Booking> bookings = bookingRepository.findAllByBerth(berth);
        return bookingConverter.toDtos(bookings);
    }

    private void validateBooking(Booking booking) {
        if (booking.getShip().getUserInfo() != booking.getRenter()) {
            throw new AccessException();
        }

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

    private Double calcTotalPrice(Booking booking) {        // todo хранить в бд
        Double price = booking.getBerthPlace().getFactPrice();
        return price * DAYS.between(booking.getStartDate(), booking.getEndDate());
    }
}
