package app.service.account.dto;

import app.service.file.dto.FileInfoDto;
import lombok.Data;

@Data
public class UserInfoDto {

    private String firstName;
    private String lastName;
    private String phCode;
    private String phNumber;
    private FileInfoDto photo;

    @Data
    public static class Resp extends UserInfoDto {

        private Long accountId;
    }
}
