package ru.hse.coursework.berth.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.coursework.berth.websocket.event.incoming.ChatMessageIncomingDto;
import ru.hse.coursework.berth.websocket.event.incoming.ChatOffsetIncomingDto;
import ru.hse.coursework.berth.websocket.event.outgoing.ChatMessageOutgoingDto;
import ru.hse.coursework.berth.websocket.event.outgoing.ReviewPublishOutgoingDto;


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

        private ChatMessageOutgoingDto CHAT_MESSAGE;
        private ReviewPublishOutgoingDto REVIEW_PUBLISH;
    }
}
