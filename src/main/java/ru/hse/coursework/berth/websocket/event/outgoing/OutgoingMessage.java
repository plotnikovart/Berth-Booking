package ru.hse.coursework.berth.websocket.event.outgoing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.websocket.event.OutgoingEventEnum;

@Data
public class OutgoingMessage {

    @ApiModelProperty(required = true, position = 1)
    private OutgoingEventEnum event;
}
