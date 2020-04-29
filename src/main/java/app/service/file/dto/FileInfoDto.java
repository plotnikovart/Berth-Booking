package app.service.file.dto;

import app.config.validation.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
