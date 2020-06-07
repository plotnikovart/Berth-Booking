package ru.hse.coursework.berth.websocket.event.outgoing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.websocket.event.OutgoingEventEnum;
import ru.hse.coursework.berth.websocket.event.OutgoingMessage;

public class BookingCancelOutgoingDto extends OutgoingMessage<BookingCancelOutgoingDto.D> {

    public BookingCancelOutgoingDto(D data) {
        super(OutgoingEventEnum.BOOKING_CANCEL, data);
    }

    @Data
    @ApiModel("BookingCancelOutgoingDtoData")
    public static class D {

        @ApiModelProperty(required = true, position = 10)
        private Long bookingId;
    }
}
