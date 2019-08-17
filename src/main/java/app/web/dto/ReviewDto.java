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

    private UserInfoDto userInfo;

    private Long berthId;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = 0, max = 100, message = ValidationUtils.RANGE_MESSAGE)
    private Integer rating;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 1024, message = ValidationUtils.LENGTH_MESSAGE)
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateTime;

    @Data
    public static class WithId extends ReviewDto {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        private Long id;
    }
}
