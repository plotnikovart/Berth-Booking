package ru.hse.coursework.berth.websocket.event.outgoing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;

@Data
public class ChatMessageOutgoingDto extends OutgoingMessage {

    @ApiModelProperty(required = true, position = 10)
    private D data;

    @Data
    @ApiModel("ChatMessageOutgoingDtoData")
    public static class D {

        @ApiModelProperty(required = true, position = 10)
        private Long chatId;

        @ApiModelProperty(required = true, position = 11)
        private MessageDto.Resp message;
    }
}
