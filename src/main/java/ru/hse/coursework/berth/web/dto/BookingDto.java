package ru.hse.coursework.berth.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.enums.BookingStatus;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.berth.dto.BerthDto;
import ru.hse.coursework.berth.service.berth.dto.BerthPlaceDto;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BookingDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date endDate;


    @Data
    public static class Req extends BookingDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long berthPlaceId;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long shipId;
    }

    @Data
    public static class Resp extends BookingDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long id;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private UserInfoDto.Resp owner;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private UserInfoDto.Resp renter;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private BerthPlaceDto berthPlace;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private BerthDto.Resp berth;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private ShipDto.Resp ship;

        private BookingStatus status;

        private Double totalPrice;
    }
}
