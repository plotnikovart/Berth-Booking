package app.service.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FileInfoDto {

    private UUID fileId;
    private String fileName;
    private String fileLink;
}
