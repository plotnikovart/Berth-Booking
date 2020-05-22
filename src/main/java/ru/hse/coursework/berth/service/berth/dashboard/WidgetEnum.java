package ru.hse.coursework.berth.service.berth.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum WidgetEnum implements EnumWithIdentifier<String> {

    YEAR_REVENUE("year_revenue"),
    RESERVED_PERCENT("reserved_percent"),
    WEEK_BOOKINGS("week_bookings"),
    RATING_TREND("rating_trend"),
    PLACE_BOOKING_MAP("place_booking_map"),
    LOCATION("location");

    private final String identifier;
}
