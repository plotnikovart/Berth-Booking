package ru.hse.coursework.berth.websocket.event.incoming;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.websocket.event.IncomingEventEnum;

import javax.validation.constraints.NotNull;

@Data
public class IncomingMessage {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @ApiModelProperty(required = true, position = 1)
    private IncomingEventEnum event;
}
