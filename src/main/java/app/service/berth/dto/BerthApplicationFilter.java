package app.service.berth.dto;

import app.database.entity.enums.BerthApplicationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static app.config.validation.ValidationUtils.NOT_NULL_MESSAGE;

@Data
public class BerthApplicationFilter {

    @ApiModelProperty(position = 0)
    private Boolean onlyMy;

    @ApiModelProperty(position = 1)
    private BerthApplicationStatus status;

    @ApiModelProperty(position = 2)
    private LocalDateTime dateFrom;

    @ApiModelProperty(position = 3)
    private LocalDateTime dateTo;

    @ApiModelProperty(required = true, position = 10)
    @NotNull(message = NOT_NULL_MESSAGE)
    private Integer pageNum;

    @ApiModelProperty(required = true, position = 11)
    @NotNull(message = NOT_NULL_MESSAGE)
    private Integer pageSize;
}
