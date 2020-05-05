package app.service.berth.dto;

import app.database.entity.enums.BerthApplicationStatus;
import app.service.file.dto.FileInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static app.config.validation.ValidationUtils.NOT_NULL_MESSAGE;

@Data
public class BerthApplicationDto {

    @ApiModelProperty(position = 6)
    private String description;

    @ApiModelProperty(position = 7)
    private List<FileInfoDto> attachments;


    @Data
    @ApiModel("BerthApplicationDtoReq")
    public static class Req extends BerthApplicationDto {

        @Valid
        @ApiModelProperty(required = true, position = 8)
        @NotNull(message = NOT_NULL_MESSAGE)
        private BerthDto berth;
    }

    @Data
    @ApiModel("BerthApplicationDtoResp")
    public static class Resp extends BerthApplicationDto {

        @ApiModelProperty(required = true, position = 0)
        private Long id;

        @ApiModelProperty(required = true, position = 1)
        private Long berthId;

        @ApiModelProperty(required = true, position = 2)
        private Long applicantId;

        @ApiModelProperty(required = true, position = 3)
        private LocalDateTime createdAt;

        @ApiModelProperty(required = true, position = 4)
        private BerthApplicationStatus status;

        @ApiModelProperty(required = true, position = 5)
        private String title;

        @ApiModelProperty(required = true, position = 20)
        private String decision;

        @ApiModelProperty(required = true, position = 21)
        private Long moderatorId;
    }
}
