package ru.hse.coursework.berth.service.berth.dashboard.widget.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;

@Data
public class PlaceBookingMapDto {

    @ApiModelProperty(required = true, position = 1)
    private BerthPlaceDto place;

    @ApiModelProperty(position = 2, notes = "Если не указан, то значит место не забронировано")
    private Long bookingId;
}
