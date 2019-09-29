package app.web.dto;

import app.common.ValidationUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReviewDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Long berthId;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = 0, max = 100, message = ValidationUtils.RANGE_MESSAGE)
    private Integer rating;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 1024, message = ValidationUtils.LENGTH_MESSAGE)
    private String text;


    @Data
    public static class Req extends ReviewDto {
    }

    @Data
    public static class Resp extends ReviewDto {

        private Long id;

        private UserInfoDto userInfo;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime dateTime;
    }
}
