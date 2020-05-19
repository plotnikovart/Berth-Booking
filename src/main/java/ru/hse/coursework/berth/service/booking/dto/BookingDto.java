package ru.hse.coursework.berth.service.booking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.ship.dto.ShipDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BookingDto {

    @ApiModelProperty(required = true, position = 20)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private LocalDate startDate;

    @ApiModelProperty(required = true, position = 21)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private LocalDate endDate;


    @Data
    public static class Req extends BookingDto {

        @ApiModelProperty(required = true, position = 1)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long berthPlaceId;

        @ApiModelProperty(required = true, position = 2)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long shipId;
    }

    @Data
    public static class RespRenter extends BookingDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long id;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private BerthDto.Resp berth;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private String berthPlaceName;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private ShipDto.Resp ship;

        private BookingStatus status;

        private Double totalPrice;
    }
}
