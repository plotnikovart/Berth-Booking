package ru.hse.coursework.berth.service.berth.dashboard.widget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.common.DateHelper;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetService;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.DateReservedPercentDto;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.ReservedPercentDto;

import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeekReservedPercentWidgetService implements WidgetService<List<DateReservedPercentDto>> {

    private final Clock clock;
    private final BerthRepository berthRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<DateReservedPercentDto> getWidgetData(Long berthId) {
        Berth berth = berthRepository.getOne(berthId);
        Integer placesNum = berthRepository.countBerthPlaces(berth);

        LocalDate startDate = LocalDate.now(clock);
        LocalDate endDate = LocalDate.now(clock).plusDays(6);

        List<Booking> bookings = bookingRepository.findPayedBookingsByDates(berth, startDate, endDate);

        List<DateReservedPercentDto> result = new LinkedList<>();
        for (int i = 0; ; i++) {
            LocalDate date = startDate.plusDays(i);
            long dateBookingsNum = bookings.stream()
                    .filter(it -> DateHelper.isIntersect(date, date, it.getStartDate(), it.getEndDate()))
                    .count();

            var dto = new DateReservedPercentDto()
                    .setDate(date)
                    .setPercent(new ReservedPercentDto()
                            .setReservedPlaceNum((int) dateBookingsNum)
                            .setTotalPlaceNum(placesNum));
            result.add(dto);

            if (date.equals(endDate)) {
                break;
            }
        }

        return result;
    }

    @Override
    public WidgetEnum getWidgetEnum() {
        return WidgetEnum.WEEK_RESERVED_PERCENT;
    }
}
