package app.service.berth.dto;

import app.service.file.dto.FileInfoDto;
import io.swagger.annotations.ApiModel;
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

    @ApiModelProperty(required = true, position = 3)
    @NotNull(message = NOT_NULL_MESSAGE)
    @Length(max = 64, message = LENGTH_MESSAGE)
    private String name;

    @ApiModelProperty(position = 4)
    @Length(max = 2048, message = LENGTH_MESSAGE)
    private String description;

    @ApiModelProperty(required = true, position = 5)
    @NotNull(message = NOT_NULL_MESSAGE)
    @Range(min = -90, max = 90, message = RANGE_MESSAGE)
    private Double lat;     // широта

    @ApiModelProperty(required = true, position = 6)
    @NotNull(message = NOT_NULL_MESSAGE)
    @Range(min = -180, max = 180, message = RANGE_MESSAGE)
    private Double lng;     // долгота

    @ApiModelProperty(position = 7)
    @Length(max = 256, message = LENGTH_MESSAGE)
    private String site;

    @ApiModelProperty(position = 8)
    @Length(max = 32, message = LENGTH_MESSAGE)
    private String radio;

    @ApiModelProperty(position = 9)
    @Length(max = 3, message = LENGTH_MESSAGE)
    private String phCode;

    @ApiModelProperty(position = 10)
    @Length(max = 20, message = LENGTH_MESSAGE)
    private String phNumber;

    @Valid
    @ApiModelProperty(position = 11, notes = "Не передавать, если изменение не нужно")
    private List<FileInfoDto> photos;

    @Valid
    @ApiModelProperty(position = 12, notes = "Не передавать, если изменение не нужно")
    private List<DictAmenityDto> amenities;

    @Valid
    @ApiModelProperty(position = 13, notes = "Не передавать, если изменение не нужно")
    private List<BerthPlaceDto> places;


    @Data
    @ApiModel("BerthDtoResp")
    public static class Resp extends BerthDto {

        @ApiModelProperty(required = true, position = 0)
        private Long id;
        @ApiModelProperty(required = true, position = 1)
        private Boolean isConfirmed;
        @ApiModelProperty(required = true, position = 2)
        private Integer rating;


        @Data
        public static class Search extends Resp {

            private Double distance;
            private Double minPrice;
        }
    }
}
