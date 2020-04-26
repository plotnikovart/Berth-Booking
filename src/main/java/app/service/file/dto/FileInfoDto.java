package app.service.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {

    private UUID fileId;
    private String fileName;
    private String fileLink;
}
