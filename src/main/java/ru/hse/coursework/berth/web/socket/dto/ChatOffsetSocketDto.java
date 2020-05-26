package ru.hse.coursework.berth.web.socket.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ChatOffsetSocketDto extends SocketMessage {

    @Valid
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    @ApiModelProperty(required = true, position = 10)
    private D data;

    @Data
    @ApiModel("ChatOffsetSocketDtoData")
    public static class D {

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true, position = 10)
        private Long chatId;

        @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
        @ApiModelProperty(required = true, position = 11)
        private Long offset;
    }
}
