package app.service.berth.dto;

import app.service.file.dto.FileInfoDto;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class BerthApplicationDto {

    private String description;
    private List<FileInfoDto> attachemants;

    @Valid
    private BerthDto berth;
}
