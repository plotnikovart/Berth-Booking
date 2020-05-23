package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import java.util.List;

/**
 * Класс для описания свагером всех выходных моделей для виджетов
 */
public class AllWidgetsModel {

    private LocationDto location;
    private List<PlaceBookingMapDto> place_booking_map;
    private RatingTrendDto rating_trend;
    private ReservedPercentDto reserved_percent;
    private List<DateReservedPercentDto> week_reserved_percent;
    private List<MonthRevenueDto> year_revenue;
}
