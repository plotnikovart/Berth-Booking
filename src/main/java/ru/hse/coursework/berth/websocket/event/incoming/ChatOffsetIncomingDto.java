package ru.hse.coursework.berth.websocket.event.incoming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.websocket.event.IncomingMessage;

import javax.validation.constraints.NotNull;

@Data
public class ChatOffsetIncomingDto extends IncomingMessage<ChatOffsetIncomingDto.D> {

    @Data
    @ApiModel("ChatOffsetIncomingDtoData")
    public static class D {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true, position = 10)
        private Long chatId;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true, position = 11)
        private Long offset;
    }
}
