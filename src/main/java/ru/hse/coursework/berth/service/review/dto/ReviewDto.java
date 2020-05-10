package ru.hse.coursework.berth.service.review.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ReviewDto {

    @ApiModelProperty(required = true, position = 10)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = 0, max = 100, message = ValidationUtils.RANGE_MESSAGE)
    private Integer rating;

    @ApiModelProperty(position = 12)
    @Length(max = 1024, message = ValidationUtils.LENGTH_MESSAGE)
    private String text;

    @Data
    @ApiModel("ReviewDtoResp")
    public static class Resp extends ReviewDto {

        @ApiModelProperty(required = true, position = -1)
        private Long id;

        @ApiModelProperty(required = true, position = 2)
        private ReviewerDto reviewer;

        @ApiModelProperty(required = true, position = 3)
        private LocalDate dateTime;
    }
}
