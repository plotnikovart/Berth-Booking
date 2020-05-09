package ru.hse.coursework.berth.service.berth.dto.management;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.database.entity.enums.BerthApplicationStatus;

@Data
public class ChangeApplicationStatusResp {

    @ApiModelProperty(required = true)
    private BerthApplicationStatus newStatus;
}
