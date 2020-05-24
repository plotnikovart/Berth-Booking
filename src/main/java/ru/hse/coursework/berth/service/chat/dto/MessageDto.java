package ru.hse.coursework.berth.service.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.config.validation.ValidationUtils;
import ru.hse.coursework.berth.database.entity.chat.enums.MessageType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class MessageDto {

    @ApiModelProperty(required = true, position = 10)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private MessageType type;

    @ApiModelProperty(required = true, position = 11)
    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private String text;

    @Data
    @ApiModel("MessageDtoResp")
    public static class Resp extends MessageDto {

        @ApiModelProperty(required = true, position = 1)
        private Long id;

        @ApiModelProperty(required = true, position = 2)
        private Long offset;

        @ApiModelProperty(required = true, position = 3)
        private LocalDateTime sendDateTime;

        @ApiModelProperty(required = true, position = 4)
        private Long participantId;
    }
}
