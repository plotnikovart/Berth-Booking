package ru.hse.coursework.berth.service.berth.dashboard.widget;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetService;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.ReservedPercentDto;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservedPercentWidgetService implements WidgetService<ReservedPercentDto> {

    private final Clock clock;  // for tests
    private final BerthRepository berthRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ReservedPercentDto getWidgetData(Long berthId) {
        Berth berth = berthRepository.getOne(berthId);

        Integer totalPlaces = berthRepository.countBerthPlaces(berth);
        Integer bookedPlaces = bookingRepository.countPayedBookingsByDate(berth, LocalDate.now(clock));

        return new ReservedPercentDto()
                .setReservedPlaceNum(bookedPlaces)
                .setTotalPlaceNum(totalPlaces);
    }

    @Override
    public WidgetEnum getWidgetEnum() {
        return WidgetEnum.RESERVED_PERCENT;
    }
}
