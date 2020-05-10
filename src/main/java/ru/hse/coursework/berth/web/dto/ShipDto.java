package ru.hse.coursework.berth.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.enums.ShipType;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ShipDto {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @Length(max = 256, message = ValidationUtils.LENGTH_MESSAGE)
    private String name;

    @ApiModelProperty(position = 2, required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private ShipType type;

    @ApiModelProperty(position = 3, required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Dimensions dimensions;

    @ApiModelProperty(position = 4, required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Model model;

    @ApiModelProperty(position = 5, required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Registration registration;

    @ApiModelProperty(position = 6, required = true)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private Insurance insurance;

    @ApiModelProperty(position = 7, notes = "Если не передать этот параметр, то фотографии не будут изменены")
    private List<FileInfoDto> photos;


    @ApiModel("ShipDtoResp")
    public static class Resp extends ShipDto {

        @ApiModelProperty(required = true, position = -1)
        private Long id;
    }

    @Data
    public static class Dimensions implements Serializable {

        @ApiModelProperty(required = true, position = 1)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @Range(message = ValidationUtils.RANGE_MESSAGE)
        private Double length;

        @ApiModelProperty(required = true, position = 2)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @Range(message = ValidationUtils.RANGE_MESSAGE)
        private Double draft;

        @ApiModelProperty(required = true, position = 3)
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @Range(message = ValidationUtils.RANGE_MESSAGE)
        private Double width;
    }

    @Data
    public static class Model implements Serializable {

        @ApiModelProperty(position = 1)
        private String producer;

        @ApiModelProperty(position = 2)
        private String model;

        @ApiModelProperty(position = 3)
        private Integer year;
    }

    @Data
    public static class Registration implements Serializable {

        @ApiModelProperty(position = 1)
        private String number;

        @ApiModelProperty(position = 2)
        private LocalDate expire;

        @ApiModelProperty(position = 3)
        private FileInfoDto file;
    }

    @Data
    public static class Insurance implements Serializable {

        @ApiModelProperty(position = 1)
        private String company;

        @ApiModelProperty(position = 2)
        private String number;

        @ApiModelProperty(position = 3)
        private LocalDate expire;

        @ApiModelProperty(position = 4)
        private FileInfoDto file;
    }
}
