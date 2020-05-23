package ru.hse.coursework.berth.service.berth.dashboard.widget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.DateReservedPercentDto;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.ReservedPercentDto;

import java.time.LocalDate;
import java.util.List;

class WeekReservedPercentWidgetServiceTest extends DashboardBaseTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    WeekReservedPercentWidgetService weekReservedPercentWidgetService;

    @Test
    void test() {
        createBooking(berth1Place1, LocalDate.of(2020, 6, 21), LocalDate.of(2020, 6, 25), BookingStatus.PAYED);
        createBooking(berth1Place2, LocalDate.of(2020, 6, 21), LocalDate.of(2020, 6, 25), BookingStatus.APPROVED);
        createBooking(berth1Place3, LocalDate.of(2020, 1, 21), LocalDate.of(2020, 2, 25), BookingStatus.PAYED);
        createBooking(berth1Place3, LocalDate.of(2020, 6, 22), LocalDate.of(2020, 6, 26), BookingStatus.PAYED);

        var expected = List.of(
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 21))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(1)),
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 22))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(2)),
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 23))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(2)),
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 24))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(2)),
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 25))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(2)),
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 26))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(1)),
                new DateReservedPercentDto()
                        .setDate(LocalDate.of(2020, 6, 27))
                        .setPercent(new ReservedPercentDto()
                                .setTotalPlaceNum(3)
                                .setReservedPlaceNum(0))
        );


        var actual = weekReservedPercentWidgetService.getWidgetData(berth1.getId());

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