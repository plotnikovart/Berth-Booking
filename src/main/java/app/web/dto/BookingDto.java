package app.web.dto;

import app.common.ValidationUtils;
import app.database.entity.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BookingDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private UserInfoDto.WithId owner;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private UserInfoDto.WithId renter;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private BerthPlaceDto.WithId berthPlace;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private ShipDto.WithId ship;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date endDate;

    private BookingStatus status;

    @Data
    public static class WithId extends BookingDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long id;
    }
}
