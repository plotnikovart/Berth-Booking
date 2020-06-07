package ru.hse.coursework.berth.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.coursework.berth.websocket.event.incoming.ChatMessageIncomingDto;
import ru.hse.coursework.berth.websocket.event.incoming.ChatOffsetIncomingDto;
import ru.hse.coursework.berth.websocket.event.outgoing.*;


@Api(value = "Описание моделей событий для web socket")
@RestController
@RequestMapping("/api/web-socket/model")
public class WebSocketModelController {

    @DeleteMapping("incoming")
    IncomingModel incomingModel() {
        return new IncomingModel();
    }

    @DeleteMapping("outgoing")
    OutgoingModel outgoingModel() {
        return new OutgoingModel();
    }


    public static class IncomingModel {

        private ChatMessageIncomingDto CHAT_MESSAGE;
        private ChatOffsetIncomingDto CHAT_OFFSET;
    }

    public static class OutgoingModel {

        @ApiModelProperty(position = 1)
        private ChatMessageOutgoingDto CHAT_MESSAGE;

        @ApiModelProperty(position = 2)
        private ReviewPublishOutgoingDto REVIEW_PUBLISH;

        @ApiModelProperty(position = 3)
        private BookingOpenOutgoingDto BOOKING_OPEN;

        @ApiModelProperty(position = 4)
        private BookingApproveOutgoingDto BOOKING_APPROVE;

        @ApiModelProperty(position = 5)
        private BookingRejectOutgoingDto BOOKING_REJECT;

        @ApiModelProperty(position = 6)
        private BookingPayOutgoingDto BOOKING_PAY;

        @ApiModelProperty(position = 7)
        private BookingCancelOutgoingDto BOOKING_CANCEL;
    }
}
