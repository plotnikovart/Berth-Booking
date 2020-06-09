package ru.hse.coursework.berth.service.booking.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class BookingSearchReq {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = -90, max = 90, message = ValidationUtils.RANGE_MESSAGE)
    private Double lat;     // широта

    @ApiModelProperty(required = true, position = 2)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = -180, max = 180, message = ValidationUtils.RANGE_MESSAGE)
    private Double lng;     // долгота

    @ApiModelProperty(required = true, position = 3)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double rad;

    @ApiModelProperty(position = 4)
    private LocalDate startDate;

    @ApiModelProperty(position = 5)
    private LocalDate endDate;

    @ApiModelProperty(position = 6)
    private Long shipId;

    @ApiModelProperty(position = 7)
    private List<DictAmenityDto> amenities;

    @ApiModelProperty(required = true, position = 8)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Sorting sorting;
}
