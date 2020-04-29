package app.service.berth.dto;

import app.config.validation.ValidationUtils;
import app.service.file.dto.FileInfoDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.DictAmenityDto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BerthDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 64, message = ValidationUtils.LENGTH_MESSAGE)
    private String name;

    @Length(max = 2048, message = ValidationUtils.LENGTH_MESSAGE)
    private String description;

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = -90, max = 90, message = ValidationUtils.RANGE_MESSAGE)
    private Double lat;     // широта

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Range(min = -180, max = 180, message = ValidationUtils.RANGE_MESSAGE)
    private Double lng;     // долгота

    @Valid
    private List<FileInfoDto> photoList;

    @Valid
    private List<DictAmenityDto> amenityList;

    @Valid
    private List<BerthPlaceDto> placeList;


    @Data
    public static class Resp extends BerthDto {

        private Long id;
        private Integer rating;


        @Data
        public static class Search extends Resp {

            private Double distance;
            private Double minPrice;
        }
    }
}
