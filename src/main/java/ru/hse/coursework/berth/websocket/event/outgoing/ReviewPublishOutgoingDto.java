package ru.hse.coursework.berth.websocket.event.outgoing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ru.hse.coursework.berth.service.review.dto.ReviewDto;
import ru.hse.coursework.berth.websocket.event.OutgoingEventEnum;
import ru.hse.coursework.berth.websocket.event.OutgoingMessage;

public class ReviewPublishOutgoingDto extends OutgoingMessage<ReviewPublishOutgoingDto.D> {

    public ReviewPublishOutgoingDto(D data) {
        super(OutgoingEventEnum.REVIEW_PUBLISH, data);
    }

    @Data
    @ApiModel("ReviewPublishOutgoingDtoData")
    public static class D {

        @ApiModelProperty(required = true, position = 10)
        private Long berthId;

        @ApiModelProperty(required = true, position = 11)
        private ReviewDto.Resp review;
    }
}
