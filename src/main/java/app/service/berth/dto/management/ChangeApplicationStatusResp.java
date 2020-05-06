package app.service.berth.dto.management;

import app.database.entity.enums.BerthApplicationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeApplicationStatusResp {

    @ApiModelProperty(required = true)
    private BerthApplicationStatus newStatus;
}
