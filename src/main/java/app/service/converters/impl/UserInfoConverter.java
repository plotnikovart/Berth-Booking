package app.service.converters.impl;

import app.database.entity.UserInfo;
import app.service.converters.AbstractConverter;
import app.service.file.ImageKind;
import app.web.dto.UserInfoDto;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UserInfoConverter extends AbstractConverter<UserInfo, UserInfoDto.Resp, UserInfoDto.Req> {

    @Override
    public UserInfoDto.Resp toDto(UserInfoDto.Resp dto, UserInfo e) {
        var photo = e.getPhotoName() == null ?
                null : MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.USER.name().toLowerCase(), e.getAccountId(), e.getPhotoName());

        return (UserInfoDto.Resp) dto
                .setAccountId(e.getAccountId())
                .setEmail(e.getAccount().getEmail())
                .setFirstName(e.getFirstName())
                .setLastName(e.getLastName())
                .setPhCode(e.getPhCode())
                .setPhNumber(e.getPhNumber())
                .setPhoto(photo);
    }

    @Override
    public UserInfo toEntity(UserInfo entity, UserInfoDto.Req dto) {
        return entity
                .setFirstName(dto.getFirstName())
                .setLastName(dto.getLastName())
                .setPhCode(dto.getPhCode())
                .setPhNumber(dto.getPhNumber())
                .setPhotoName(dto.getPhoto());
    }
}
