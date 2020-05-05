package app.web.dto;

import app.config.validation.ValidationUtils;
import app.database.entity.enums.BookingStatus;
import app.service.account.dto.UserInfoDto;
import app.service.berth.dto.BerthDto;
import app.service.berth.dto.BerthPlaceDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
