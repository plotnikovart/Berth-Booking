package ru.hse.coursework.berth.service.berth.dashboard.widget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.ReservedPercentDto;

import java.time.LocalDate;

class ReservedPercentWidgetServiceTest extends DashboardBaseTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ReservedPercentWidgetService reservedPercentWidgetService;

    @Test
    void test() {
        createBooking(berth1Place1, LocalDate.of(2020, 6, 21), LocalDate.of(2020, 6, 25), BookingStatus.PAYED);
        createBooking(berth1Place2, LocalDate.of(2020, 6, 21), LocalDate.of(2020, 6, 25), BookingStatus.APPROVED);
        createBooking(berth1Place3, LocalDate.of(2020, 1, 21), LocalDate.of(2020, 2, 25), BookingStatus.PAYED);
        createBooking(berth1Place3, LocalDate.of(2020, 6, 22), LocalDate.of(2020, 6, 25), BookingStatus.PAYED);


        var expected = new ReservedPercentDto()
                .setTotalPlaceNum(3)
                .setReservedPlaceNum(1);

        ReservedPercentDto actual = reservedPercentWidgetService.getWidgetData(berth1.getId());

        Assertions.assertEquals(expected, actual);
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