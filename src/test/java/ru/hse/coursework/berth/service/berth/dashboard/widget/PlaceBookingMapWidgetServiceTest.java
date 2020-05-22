package ru.hse.coursework.berth.service.berth.dashboard.widget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.PlaceBookingMapDto;
import ru.hse.coursework.berth.service.converters.impl.BerthPlaceConverter;

import java.time.LocalDate;
import java.util.List;

class PlaceBookingMapWidgetServiceTest extends DashboardBaseTest {

    @Autowired
    BerthPlaceConverter berthPlaceConverter;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    PlaceBookingMapWidgetService placeBookingMapWidgetService;

    @Test
    void test() {
        Booking booking1 = createBooking(berth1Place1, LocalDate.of(2020, 6, 21), LocalDate.of(2020, 6, 25), BookingStatus.PAYED);
        Booking booking2 = createBooking(berth1Place2, LocalDate.of(2020, 6, 21), LocalDate.of(2020, 6, 25), BookingStatus.APPROVED);
        Booking booking3 = createBooking(berth1Place3, LocalDate.of(2020, 1, 21), LocalDate.of(2020, 2, 25), BookingStatus.PAYED);
        Booking booking4 = createBooking(berth1Place3, LocalDate.of(2020, 6, 20), LocalDate.of(2020, 6, 21), BookingStatus.PAYED);

        var expected = List.of(
                new PlaceBookingMapDto()
                        .setPlace(berthPlaceConverter.toDto(berth1Place1))
                        .setBookingId(booking1.getId()),
                new PlaceBookingMapDto()
                        .setPlace(berthPlaceConverter.toDto(berth1Place2))
                        .setBookingId(null),
                new PlaceBookingMapDto()
                        .setPlace(berthPlaceConverter.toDto(berth1Place3))
                        .setBookingId(booking4.getId())
        );

        var actual = placeBookingMapWidgetService.getWidgetData(berth1.getId());
        
        Assertions.assertTrue(actual.containsAll(expected) && expected.containsAll(actual));
    }


    @SuppressWarnings("deprecation")
    private Booking createBooking(BerthPlace place, LocalDate startDate, LocalDate endDate, BookingStatus status) {
        var booking = new Booking()
                .setTotalPrice(20.0)
                .setRenter(user2Account)
                .setBerthPlace(place)
                .setStatus(status)
                .setShip(ship)
                .setServiceFee(1.0)
                .setStartDate(startDate)
                .setEndDate(endDate);

        return bookingRepository.save(booking);
    }
}