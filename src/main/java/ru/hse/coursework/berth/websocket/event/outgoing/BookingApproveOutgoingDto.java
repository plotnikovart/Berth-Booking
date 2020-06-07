package ru.hse.coursework.berth.websocket.event.outgoing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.websocket.event.OutgoingEventEnum;
import ru.hse.coursework.berth.websocket.event.OutgoingMessage;

public class BookingApproveOutgoingDto extends OutgoingMessage<BookingApproveOutgoingDto.D> {

    public BookingApproveOutgoingDto(D data) {
        super(OutgoingEventEnum.BOOKING_APPROVE, data);
    }

    @Data
    @ApiModel("BookingApproveOutgoingDtoData")
    public static class D {

        @ApiModelProperty(required = true, position = 10)
        private Long bookingId;
    }
}
