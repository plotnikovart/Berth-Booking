package ru.hse.coursework.berth.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Ship;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.entity.enums.ShipType;
import ru.hse.coursework.berth.database.repository.AbstractAccountTest;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.database.repository.ShipRepository;
import ru.hse.coursework.berth.service.berth.BerthPart;
import ru.hse.coursework.berth.service.booking.dto.BookingDto;
import ru.hse.coursework.berth.service.booking.dto.BookingPayLinkResp;
import ru.hse.coursework.berth.service.booking.dto.BookingStatusResp;
import ru.hse.coursework.berth.service.converters.impl.BerthConverter;
import ru.hse.coursework.berth.service.converters.impl.BerthPlaceConverter;
import ru.hse.coursework.berth.service.converters.impl.ShipConverter;
import ru.hse.coursework.berth.service.converters.impl.UserInfoConverter;
import ru.hse.coursework.berth.service.event.EventPublisher;

import java.time.LocalDate;
import java.util.List;

public class BookingControllerTest extends AbstractAccountTest {

    @Autowired
    EventPublisher eventPublisher;

    @Autowired
    BerthRepository berthRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    BookingController bookingController;

    @Autowired
    BerthConverter berthConverter;
    @Autowired
    ShipConverter shipConverter;
    @Autowired
    BerthPlaceConverter berthPlaceConverter;
    @Autowired
    UserInfoConverter userInfoConverter;

    private Berth berth;
    private BerthPlace place1;
    private BerthPlace place2;

    private Ship ship1;
    private Ship ship2;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        bookingRepository.deleteAll();
        berthRepository.deleteAll();

        OperationContext.accountId(user1Account.getId());

        berth = new Berth()
                .setOwner(user1Account)
                .setIsConfirmed(true)
                .setLng(0.0)
                .setLat(0.0)
                .setName("Причал");

        place1 = new BerthPlace()
                .setBerth(berth)
                .setName("Place 1")
                .setPrice(10.0)
                .setDraft(5.0)
                .setLength(5.0)
                .setWidth(5.0)
                .setColor("")
                .setRotate(0.0)
                .setXCoord(0.0)
                .setYCoord(0.0);

        place2 = new BerthPlace()
                .setBerth(berth)
                .setName("Place 2")
                .setPrice(20.0)
                .setDraft(15.0)
                .setLength(15.0)
                .setWidth(15.0)
                .setColor("")
                .setRotate(0.0)
                .setXCoord(0.0)
                .setYCoord(0.0);

        berth.setBerthPlaces(List.of(place1, place2));
        berthRepository.save(berth);

        ship1 = new Ship()
                .setOwner(user1Account)
                .setName("Ship 1")
                .setType(ShipType.POWER)
                .setDraft(7.0)
                .setLength(7.0)
                .setWidth(7.0);

        shipRepository.save(ship1);

        OperationContext.accountId(user2Account.getId());

        ship2 = new Ship()
                .setOwner(user2Account)
                .setName("Ship 1")
                .setType(ShipType.POWER)
                .setDraft(7.0)
                .setLength(7.0)
                .setWidth(7.0);

        shipRepository.save(ship2);
    }

    @Test
    @SuppressWarnings("deprecation")
    void testBookingValidation() {
        OperationContext.accountId(user2Account.getId());

        // check place size
        var req1 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place1.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().plusDays(1))
                .setEndDate(LocalDate.now().plusDays(3));

        Assertions.assertThrows(ServiceException.class, () -> bookingController.openBooking(req1));

        req1.setBerthPlaceId(place2.getId());
        long bookingId1 = bookingController.openBooking(req1).getData().getId();


        // check start and end date validity
        var req2 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().plusDays(1))
                .setEndDate(LocalDate.now().minusDays(3));

        Assertions.assertThrows(ServiceException.class, () -> bookingController.openBooking(req2));

        req2.setEndDate(LocalDate.now().plusDays(3));
        long bookingId2 = bookingController.openBooking(req2).getData().getId();


        // check past date
        var req3 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().minusDays(10))
                .setEndDate(LocalDate.now().plusDays(10));

        Assertions.assertThrows(ServiceException.class, () -> bookingController.openBooking(req3));

        req3.setStartDate(LocalDate.now().plusDays(3));
        long bookingId3 = bookingController.openBooking(req3).getData().getId();


        // check foreign ship
        var req4 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship1.getId())
                .setStartDate(LocalDate.now().plusDays(1))
                .setEndDate(LocalDate.now().plusDays(10));

        Assertions.assertThrows(AccessException.class, () -> bookingController.openBooking(req4));


        // check already booked
        var booking3 = bookingRepository.findById(bookingId3).orElseThrow(NotFoundException::new);
        booking3.setStatus(BookingStatus.PAYED);
        bookingRepository.save(booking3);

        var req5 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(req3.getStartDate().plusDays(1))
                .setEndDate(req3.getEndDate().plusDays(2));

        Assertions.assertThrows(ServiceException.class, () -> bookingController.openBooking(req5));

        booking3.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking3);

        bookingController.openBooking(req5);
    }

    @Test
    void rejectScenario() {
        OperationContext.accountId(user2Account.getId());

        // open booking
        var req1 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().plusDays(1))
                .setEndDate(LocalDate.now().plusDays(3));


        BookingDto.RespRenter actual = bookingController.openBooking(req1).getData();

        BookingDto.RespRenter expected = (BookingDto.RespRenter) new BookingDto.RespRenter()
                .setId(actual.getId())
                .setStatus(BookingStatus.NEW)
                .setBerth(berthConverter.toDto(berth, BerthPart.AMENITIES))
                .setShip(shipConverter.toDto(ship2))
                .setBerthPlaceName(place2.getName())
                .setServiceFee(place2.getPrice() * 3 * 0.05)
                .setTotalPrice(place2.getPrice() * 3)
                .setCreatedAt(actual.getCreatedAt())
                .setStartDate(req1.getStartDate())
                .setEndDate(req1.getEndDate());

        Assertions.assertEquals(expected, actual);

        // get all my bookings
        List<BookingDto.RespRenter> actual2 = bookingController.getAllBookings().getData();
        Assertions.assertEquals(1, actual2.size());
        Assertions.assertEquals(expected, actual2.get(0));


        // owner get bookings
        Assertions.assertThrows(AccessException.class, () -> bookingController.getBookingsForBerth(berth.getId()));

        OperationContext.accountId(user1Account.getId());
        List<BookingDto.RespOwner> actual3List = bookingController.getBookingsForBerth(berth.getId()).getData();
        Assertions.assertEquals(1, actual3List.size());

        BookingDto.RespOwner actual3 = actual3List.get(0);

        BookingDto.RespOwner expected3 = (BookingDto.RespOwner) new BookingDto.RespOwner()
                .setId(actual3.getId())
                .setRenter(userInfoConverter.toDto(user2Info))
                .setBerthPlace(berthPlaceConverter.toDto(place2))
                .setShip(shipConverter.toDto(ship2))
                .setTotalPrice(expected.getTotalPrice())
                .setStatus(expected.getStatus())
                .setCreatedAt(actual3.getCreatedAt())
                .setStartDate(expected.getStartDate())
                .setEndDate(expected.getEndDate());

        Assertions.assertEquals(expected3, actual3);

        // owner reject booking
        BookingStatusResp actual4 = bookingController.rejectBooking(actual.getId()).getData();
        Assertions.assertEquals(BookingStatus.REJECTED, actual4.getNewStatus());
        Assertions.assertEquals(BookingStatus.REJECTED, bookingRepository.findById(actual.getId()).get().getStatus());
    }

    @Test
    void approveScenario() {
        OperationContext.accountId(user2Account.getId());

        // open bookings
        var req1 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().plusDays(1))
                .setEndDate(LocalDate.now().plusDays(3));

        var req2 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().plusDays(2))
                .setEndDate(LocalDate.now().plusDays(4));

        var req3 = (BookingDto.Req) new BookingDto.Req()
                .setBerthPlaceId(place2.getId())
                .setShipId(ship2.getId())
                .setStartDate(LocalDate.now().plusDays(30))
                .setEndDate(LocalDate.now().plusDays(40));

        long booking1 = bookingController.openBooking(req1).getData().getId();
        long booking2 = bookingController.openBooking(req2).getData().getId();
        long booking3 = bookingController.openBooking(req3).getData().getId();

        // renter get bookings
        List<BookingDto.RespRenter> renterBookings = bookingController.getAllBookings().getData();
        Assertions.assertEquals(3, renterBookings.size());
        Assertions.assertEquals(booking3, (long) renterBookings.get(0).getId());



        // owner get bookings
        OperationContext.accountId(user1Account.getId());
        List<BookingDto.RespOwner> ownerBookings = bookingController.getBookingsForBerth(berth.getId()).getData();
        Assertions.assertEquals(3, ownerBookings.size());
        Assertions.assertEquals(booking3, (long) ownerBookings.get(0).getId());

        // approve bookings
        BookingStatus status1 = bookingController.approveBooking(booking1).getData().getNewStatus();
        Assertions.assertEquals(BookingStatus.APPROVED, status1);

        BookingStatus status2 = bookingController.approveBooking(booking2).getData().getNewStatus();
        Assertions.assertEquals(BookingStatus.NEW, status2);


        // pay booking
        OperationContext.accountId(user2Account.getId());

        BookingPayLinkResp payLinkResp = bookingController.payBooking(booking1).getData();
        eventPublisher.payBooking(booking1);

        Assertions.assertEquals(BookingStatus.PAYED, bookingRepository.findById(booking1).get().getStatus());
        Assertions.assertEquals(BookingStatus.REJECTED, bookingRepository.findById(booking2).get().getStatus());
        Assertions.assertEquals(BookingStatus.NEW, bookingRepository.findById(booking3).get().getStatus());


        // cancel booking
        status1 = bookingController.cancelBooking(booking1).getData().getNewStatus();
        Assertions.assertEquals(BookingStatus.CANCELLED, status1);
        Assertions.assertEquals(BookingStatus.CANCELLED, bookingRepository.findById(booking1).get().getStatus());

        Assertions.assertThrows(ServiceException.class, () -> bookingController.cancelBooking(booking2));

        BookingStatus status3 = bookingController.cancelBooking(booking3).getData().getNewStatus();
        Assertions.assertEquals(BookingStatus.CANCELLED, status3);
        Assertions.assertEquals(BookingStatus.CANCELLED, bookingRepository.findById(booking3).get().getStatus());
    }
}
