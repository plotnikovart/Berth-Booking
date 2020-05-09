package ru.hse.coursework.berth.service.account.dto;

import lombok.Data;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

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
