package ru.hse.coursework.berth.service.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.berth.dto.DictAmenityDto;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BookingSearchReq {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = -90, max = 90, message = ValidationUtils.RANGE_MESSAGE)
    private Double lat;     // широта

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = 0, max = 180, message = ValidationUtils.RANGE_MESSAGE)
    private Double lng;     // долгота

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Double rad;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Long shipId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Date endDate;

    private List<DictAmenityDto> convenienceList = new ArrayList<>();

    private Sorting sorting = Sorting.DISTANCE;
}
