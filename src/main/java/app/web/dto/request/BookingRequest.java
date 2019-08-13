package app.web.dto.request;

import app.common.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BookingRequest {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Long berthPlaceId;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Long shipId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date endDate;
}
