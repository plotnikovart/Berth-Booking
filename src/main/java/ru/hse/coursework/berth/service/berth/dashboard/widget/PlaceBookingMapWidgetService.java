package ru.hse.coursework.berth.service.berth.dashboard.widget;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Berth;
import ru.hse.coursework.berth.database.entity.BerthPlace;
import ru.hse.coursework.berth.database.entity.Booking;
import ru.hse.coursework.berth.database.repository.BerthRepository;
import ru.hse.coursework.berth.database.repository.BookingRepository;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetEnum;
import ru.hse.coursework.berth.service.berth.dashboard.WidgetService;
import ru.hse.coursework.berth.service.berth.dashboard.widget.dto.PlaceBookingMapDto;
import ru.hse.coursework.berth.service.converters.impl.BerthPlaceConverter;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static one.util.streamex.StreamEx.of;

@Service
@RequiredArgsConstructor
public class PlaceBookingMapWidgetService implements WidgetService<List<PlaceBookingMapDto>> {

    private final Clock clock;
    private final BerthPlaceConverter berthPlaceConverter;
    private final BerthRepository berthRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<PlaceBookingMapDto> getWidgetData(Long berthId) {
        Berth berth = berthRepository.findById(berthId).orElseThrow(NotFoundException::new);

        Map<BerthPlace, Long> bookings = of(bookingRepository.findPayedBookingsByDates(berth, LocalDate.now(clock), LocalDate.now(clock)))
                .toMap(Booking::getBerthPlace, Booking::getId);

        return StreamEx.of(berth.getBerthPlaces())
                .map(it -> new PlaceBookingMapDto()
                        .setPlace(berthPlaceConverter.toDto(it))
                        .setBookingId(bookings.get(it))
                )
                .toList();
    }

    @Override
    public WidgetEnum getWidgetEnum() {
        return WidgetEnum.PLACE_BOOKING_MAP;
    }
}
