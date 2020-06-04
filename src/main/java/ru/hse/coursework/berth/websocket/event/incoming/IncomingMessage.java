package ru.hse.coursework.berth.websocket.event.incoming;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.websocket.event.IncomingEventEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class IncomingMessage<D> {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @ApiModelProperty(required = true, position = 1)
    private IncomingEventEnum event;

    @Valid
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @ApiModelProperty(required = true, position = 10)
    private D data;
}
