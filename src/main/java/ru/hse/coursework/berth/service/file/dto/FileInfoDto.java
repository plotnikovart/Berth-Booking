package ru.hse.coursework.berth.service.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hse.coursework.berth.config.validation.ValidationUtils;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {

    @NotNull(message = ValidationUtils.NOT_NULL_MESSAGE)
    private UUID fileId;
    private String fileName;
    private String fileLink;
}
