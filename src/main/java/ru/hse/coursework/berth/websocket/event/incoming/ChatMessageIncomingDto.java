package ru.hse.coursework.berth.websocket.event.incoming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.websocket.event.IncomingMessage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ChatMessageIncomingDto extends IncomingMessage<ChatMessageIncomingDto.D> {

    @Data
    @ApiModel("ChatMessageIncomingDtoData")
    public static class D {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true, position = 10)
        private Long chatId;

        @Valid
        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true, position = 11)
        private MessageDto message;
    }
}
