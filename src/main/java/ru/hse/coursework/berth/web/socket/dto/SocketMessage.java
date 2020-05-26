package ru.hse.coursework.berth.web.socket.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.web.socket.SocketEvent;

import javax.validation.constraints.NotNull;

@Data
public class SocketMessage {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @ApiModelProperty(required = true, position = 1)
    private SocketEvent event;
}
