package ru.hse.coursework.berth.websocket.event.outgoing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.websocket.event.OutgoingEventEnum;
import ru.hse.coursework.berth.websocket.event.OutgoingMessage;

public class BookingRejectOutgoingDto extends OutgoingMessage<BookingRejectOutgoingDto.D> {

    public BookingRejectOutgoingDto(D data) {
        super(OutgoingEventEnum.BOOKING_REJECT, data);
    }

    @Data
    @ApiModel("BookingRejectOutgoingDtoData")
    public static class D {

        @ApiModelProperty(required = true, position = 10)
        private Long bookingId;
    }
}
