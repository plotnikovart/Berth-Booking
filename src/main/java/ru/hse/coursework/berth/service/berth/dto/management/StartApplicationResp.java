package ru.hse.coursework.berth.service.berth.dto.management;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StartApplicationResp extends ChangeApplicationStatusResp {

    @ApiModelProperty(required = true)
    private Long chatId;
}
