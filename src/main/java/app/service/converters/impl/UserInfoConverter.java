package app.service.converters.impl;

import app.database.entity.UserInfo;
import app.service.account.dto.UserInfoDto;
import app.service.converters.AbstractConverter;
import app.service.file.FileInfoService;
import app.service.file.dto.FileInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserInfoConverter extends AbstractConverter<UserInfo, UserInfoDto.Resp, UserInfoDto> {

    private final FileInfoService fileInfoService;

    @Override
    public UserInfoDto.Resp toDto(UserInfoDto.Resp dto, UserInfo e) {
        FileInfoDto photo = null;
        if (e.getPhoto() != null) {
            photo = fileInfoService.get(e.getPhoto());
        } else if (e.getPhotoLink() != null) {
            photo = new FileInfoDto().setFileLink(e.getPhotoLink());
        }

        return (UserInfoDto.Resp) dto
                .setAccountId(e.getAccountId())
                .setFirstName(e.getFirstName())
                .setLastName(e.getLastName())
                .setPhCode(e.getPhCode())
                .setPhNumber(e.getPhNumber())
                .setPhoto(photo);
    }

    @Override
    public UserInfo toEntity(UserInfo entity, UserInfoDto dto) {
        UUID photo = null;
        if (dto.getPhoto() != null && dto.getPhoto().getFileId() != null) {
            photo = fileInfoService.get(dto.getPhoto().getFileId()).getFileId();
        }

        return entity
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setPhCode(dto.getPhCode())
                .setPhNumber(dto.getPhNumber())
                .setPhoto(photo);
    }
}
