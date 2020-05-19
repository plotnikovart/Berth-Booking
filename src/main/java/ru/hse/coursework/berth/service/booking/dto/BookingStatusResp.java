package ru.hse.coursework.berth.service.booking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;

@AllArgsConstructor(staticName = "of")
public class BookingStatusResp {

    @ApiModelProperty(required = true)
    private BookingStatus newStatus;
}
