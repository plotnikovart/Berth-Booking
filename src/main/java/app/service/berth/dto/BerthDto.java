package app.service.berth.dto;

import app.service.file.dto.FileInfoDto;
import app.web.dto.BerthPlaceDto;
import app.web.dto.DictAmenityDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static app.config.validation.ValidationUtils.*;

@Data
public class BerthDto {

    @ApiModelProperty(required = true)
    @NotNull(message = NOT_NULL_MESSAGE)
    @Length(max = 64, message = LENGTH_MESSAGE)
    private String name;

    @Length(max = 2048, message = LENGTH_MESSAGE)
    private String description;

    @ApiModelProperty(required = true)
    @NotNull(message = NOT_NULL_MESSAGE)
    @Range(min = -90, max = 90, message = RANGE_MESSAGE)
    private Double lat;     // широта

    @ApiModelProperty(required = true)
    @NotNull(message = NOT_NULL_MESSAGE)
    @Range(min = -180, max = 180, message = RANGE_MESSAGE)
    private Double lng;     // долгота

    @Length(max = 256, message = LENGTH_MESSAGE)
    private String site;
    @Length(max = 32, message = LENGTH_MESSAGE)
    private String radio;
    @Length(max = 3, message = LENGTH_MESSAGE)
    private String phCode;
    @Length(max = 20, message = LENGTH_MESSAGE)
    private String phNumber;

    @Valid
    private List<FileInfoDto> photos;

    @Valid
    private List<DictAmenityDto> amenities;

    @Valid
    private List<BerthPlaceDto> places;


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
