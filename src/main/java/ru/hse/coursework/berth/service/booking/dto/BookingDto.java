package ru.hse.coursework.berth.service.booking.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;
import ru.hse.coursework.berth.service.ship.dto.ShipDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    @ApiModelProperty(required = true, position = 20)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private LocalDate startDate;

    @ApiModelProperty(required = true, position = 21)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private LocalDate endDate;


    @Data
    @ApiModel("BookingDtoReq")
    public static class Req extends BookingDto {

        @ApiModelProperty(required = true, position = 1)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long berthPlaceId;

        @ApiModelProperty(required = true, position = 2)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long shipId;
    }

    @Data
    @ApiModel("BookingDtoRespRenter")
    public static class RespRenter extends BookingDto {

        @ApiModelProperty(required = true, position = 1)
        private Long id;

        @ApiModelProperty(required = true, position = 2)
        private BerthDto.Resp berth;

        @ApiModelProperty(required = true, position = 3)
        private String berthPlaceName;

        @ApiModelProperty(required = true, position = 4)
        private ShipDto.Resp ship;

        @ApiModelProperty(required = true, position = 5)
        private BookingStatus status;

        @ApiModelProperty(required = true, position = 6)
        private Double totalPrice;

        @ApiModelProperty(required = true, position = 7)
        private Double serviceFee;

        @ApiModelProperty(required = true, position = 8)
        private LocalDateTime createdAt;
    }

    @Data
    @ApiModel("BookingDtoRespOwner")
    public static class RespOwner extends BookingDto {

        @ApiModelProperty(required = true, position = 1)
        private Long id;

        @ApiModelProperty(required = true, position = 2)
        private UserInfoDto.Resp renter;

        @ApiModelProperty(required = true, position = 3)
        private BerthPlaceDto berthPlace;

        @ApiModelProperty(required = true, position = 4)
        private ShipDto.Resp ship;

        @ApiModelProperty(required = true, position = 5)
        private BookingStatus status;

        @ApiModelProperty(required = true, position = 6)
        private Double totalPrice;

        @ApiModelProperty(required = true, position = 7)
        private LocalDateTime createdAt;
    }
}
