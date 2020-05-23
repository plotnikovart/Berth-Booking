package ru.hse.coursework.berth.service.berth.dashboard.widget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.DashboardBaseTest;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.MonthRevenueDto;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

class YearRevenueWidgetServiceTest extends DashboardBaseTest {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    YearRevenueWidgetService yearRevenueWidgetService;

    @Test
    void test() {
        createBooking(berth1Place2, LocalDate.of(2019, 6, 30));
        createBooking(berth1Place2, LocalDate.of(2019, 7, 1));
        createBooking(berth1Place1, LocalDate.of(2020, 1, 1));
        createBooking(berth1Place2, LocalDate.of(2020, 1, 20));
        createBooking(berth1Place2, LocalDate.of(2020, 6, 21));
        createBooking(berth1Place2, LocalDate.of(2020, 6, 30));
        createBooking(berth1Place2, LocalDate.of(2020, 7, 1));

        List<MonthRevenueDto> expected = List.of(
                createDto(2019, 7, 20.0),
                createDto(2019, 8, null),
                createDto(2019, 9, null),
                createDto(2019, 10, null),
                createDto(2019, 11, null),
                createDto(2019, 12, null),
                createDto(2020, 1, 40.0),
                createDto(2020, 2, null),
                createDto(2020, 3, null),
                createDto(2020, 4, null),
                createDto(2020, 5, null),
                createDto(2020, 6, 40.0)
        );

        List<MonthRevenueDto> actual = yearRevenueWidgetService.getWidgetData(berth1.getId());
        Assertions.assertEquals(expected, actual);

        expected = List.of(
                createDto(2019, 7, null),
                createDto(2019, 8, null),
                createDto(2019, 9, null),
                createDto(2019, 10, null),
                createDto(2019, 11, null),
                createDto(2019, 12, null),
                createDto(2020, 1, null),
                createDto(2020, 2, null),
                createDto(2020, 3, null),
                createDto(2020, 4, null),
                createDto(2020, 5, null),
                createDto(2020, 6, null)
        );
        actual = yearRevenueWidgetService.getWidgetData(berth2.getId());
        Assertions.assertEquals(expected, actual);
    }


    private MonthRevenueDto createDto(int year, int month, Double revenue) {
        return new MonthRevenueDto()
                .setYear(year)
                .setMonth(Month.of(month))
                .setRevenue(revenue);
    }


    @SuppressWarnings("deprecation")
    private Booking createBooking(BerthPlace place, LocalDate startDate) {
        var booking = new Booking()
                .setTotalPrice(20.0)
                .setRenter(user2Account)
                .setBerthPlace(place)
                .setStatus(BookingStatus.PAYED)
                .setShip(ship)
                .setServiceFee(1.0)
                .setStartDate(startDate)
                .setEndDate(startDate.plusDays(3));

        return bookingRepository.save(booking);
    }
}