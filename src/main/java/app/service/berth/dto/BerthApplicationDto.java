package app.service.berth.dto;

import app.database.entity.enums.BerthApplicationStatus;
import app.service.file.dto.FileInfoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static app.config.validation.ValidationUtils.NOT_NULL_MESSAGE;

@Data
public class BerthApplicationDto {

    private String description;

    private List<FileInfoDto> attachments;


    @Data
    public static class Req extends BerthApplicationDto {

        @Valid
        @ApiModelProperty(required = true)
        @NotNull(message = NOT_NULL_MESSAGE)
        private BerthDto berth;
    }

    @Data
    public static class Resp extends BerthApplicationDto {

        @ApiModelProperty(required = true)
        private Long id;

        @ApiModelProperty(required = true)
        private Long berthId;

        @ApiModelProperty(required = true)
        private Long applicantId;

        @ApiModelProperty(required = true)
        private LocalDateTime createdAt;

        @ApiModelProperty(required = true)
        private BerthApplicationStatus status;

        @ApiModelProperty(required = true)
        private String title;

        private String decision;

        private Long moderatorId;
    }
}
