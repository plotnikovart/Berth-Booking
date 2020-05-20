package ru.hse.coursework.berth.service.booking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class BookingPayLinkResp {

    @ApiModelProperty(required = true)
    private String link;
}
