package ru.hse.coursework.berth.service.file.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto implements Serializable {

    @ApiModelProperty(required = true, position = 1)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private UUID fileId;
    @ApiModelProperty(required = true, position = 2)
    private String fileName;
    @ApiModelProperty(required = true, position = 3)
    private String fileLink;
}
