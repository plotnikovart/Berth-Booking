package ru.hse.coursework.berth.service.review.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReviewerDto {

    @ApiModelProperty(required = true, position = -1)
    private Long accountId;

    @ApiModelProperty(position = 2)
    private String firstName;

    @ApiModelProperty(position = 3)
    private String lastName;

    @ApiModelProperty(position = 4)
    private String photoLink;
}
