package ru.hse.coursework.berth.websocket.event;

import io.swagger.annotations.ApiModelProperty;


public abstract class OutgoingMessage<D> {

    @ApiModelProperty(required = true, position = 1)
    private final OutgoingEventEnum event;

    @ApiModelProperty(required = true, position = 2)
    private final D data;

    protected OutgoingMessage(OutgoingEventEnum event, D data) {
        this.event = event;
        this.data = data;
    }
}
