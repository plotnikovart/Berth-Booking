package ru.hse.coursework.berth.service.chat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatDto {

    @ApiModelProperty(required = true, position = 1)
    private Long id;

    @ApiModelProperty(required = true, position = 2)
    private Long totalOffset;

    @ApiModelProperty(required = true, position = 3)
    private Long accountOffset;

    @ApiModelProperty(required = true, position = 4)
    private MessageDto lastMessage;

    @ApiModelProperty(required = true, position = 5)
    private List<ParticipantDto> participants;
}
