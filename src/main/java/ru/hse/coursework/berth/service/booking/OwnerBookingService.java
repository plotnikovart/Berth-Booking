package ru.hse.coursework.berth.service.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.UserInfo;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.service.PermissionService;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.booking.fsm.BookingEvent;
import ru.hse.coursework.berth.service.booking.fsm.BookingFSMHandler;
import ru.hse.coursework.berth.service.converters.impl.OwnerBookingConverter;
import ru.hse.coursework.berth.service.converters.impl.UserInfoConverter;

import java.util.List;
import java.util.Map;

import static one.util.streamex.StreamEx.of;

@Service
@RequiredArgsConstructor
public class OwnerBookingService {

    private final BookingRepository bookingRepository;
    private final UserInfoRepository userInfoRepository;
    private final BerthRepository berthRepository;
    private final PermissionService permissionService;
    private final OwnerBookingConverter converter;
    private final UserInfoConverter userInfoConverter;
    private final BookingFSMHandler bookingFSMHandler;

    public List<BookingDto.RespOwner> getBookingsForBerth(long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);
        permissionService.check(berth);

        List<Booking> bookings = bookingRepository.findAllByBerthLoadBerthPlaceAndShip(berth);
        if (bookings.isEmpty()) {
            return List.of();
        }

        List<Long> renterIds = of(bookings).map(it -> it.getRenter().getId()).toList();
        Map<Long, UserInfo> idToUserInfo = of(userInfoRepository.findAllById(renterIds))
                .toMap(UserInfo::getAccountId, it -> it);

        return of(bookings)
                .map(it -> {
                    UserInfo userInfo = idToUserInfo.getOrDefault(it.getRenter().getId(), new UserInfo().setAccount(it.getRenter()));
                    UserInfoDto.Resp userInfoDto = userInfoConverter.toDto(userInfo);

                    return converter.toDto(it).setRenter(userInfoDto);
                })
                .toList();
    }

    @Transactional
    public BookingStatus approveBooking(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        checkAccess(booking);

        bookingFSMHandler.sendEvent(booking, BookingEvent.APPROVE);
        return booking.getStatus();
    }

    @Transactional
    public BookingStatus rejectBooking(long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(NotFoundException::new);
        checkAccess(booking);

        bookingFSMHandler.sendEvent(booking, BookingEvent.REJECT);
        return booking.getStatus();
    }

    private void checkAccess(Booking booking) {
        var berth = booking.getBerthPlace().getBerth(); // todo moderator access
        if (!berth.getOwnerId().equals(OperationContext.accountId())) {
            throw new AccessException();
        }
    }
}
