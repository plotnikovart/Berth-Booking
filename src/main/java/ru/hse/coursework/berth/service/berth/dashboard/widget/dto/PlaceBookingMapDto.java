package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import lombok.Data;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;

@Data
public class PlaceBookingMapDto {

    private BerthPlaceDto place;
    private Long bookingId;
}
