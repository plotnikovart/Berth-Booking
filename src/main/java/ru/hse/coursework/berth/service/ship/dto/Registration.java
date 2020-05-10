package ru.hse.coursework.berth.service.ship.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Registration implements Serializable {

    @ApiModelProperty(position = 1)
    private String number;

    @ApiModelProperty(position = 2)
    private LocalDate expire;

    @ApiModelProperty(position = 3)
    private FileInfoDto file;
}
