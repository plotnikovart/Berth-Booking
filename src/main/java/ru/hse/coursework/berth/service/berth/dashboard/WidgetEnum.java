package ru.hse.coursework.berth.service.berth.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.hse.coursework.berth.common.enums.AbstractEnumConverter;
import ru.hse.coursework.berth.common.enums.EnumWithIdentifier;

@Getter
@AllArgsConstructor
public enum WidgetEnum implements EnumWithIdentifier<String> {

    YEAR_REVENUE("year_revenue"),
    RESERVED_PERCENT("reserved_percent"),
    WEEK_RESERVED_PERCENT("week_reserved_percent"),
    RATING_TREND("rating_trend"),
    //    YEAR_RATING_TREND("year_rating_rend"),
    PLACE_BOOKING_MAP("place_booking_map"),
    LOCATION("location");

    private final String identifier;

    public static class Converter extends AbstractEnumConverter<WidgetEnum, String> {
    }
}
