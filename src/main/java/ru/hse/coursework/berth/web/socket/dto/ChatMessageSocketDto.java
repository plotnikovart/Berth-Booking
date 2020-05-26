package ru.hse.coursework.berth.web.socket.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ChatMessageSocketDto extends SocketMessage {

    @Valid
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @ApiModelProperty(required = true, position = 10)
    private D data;

    @Data
    @ApiModel("ChatMessageSocketDtoData")
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
