package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.UserInfo;
import app.database.repository.AccountRepository;
import app.database.repository.UserInfoRepository;
import app.service.converters.impl.UserInfoConverter;
import app.web.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserInfoFacade {

    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoConverter converter;

    @Transactional(readOnly = true)
    public UserInfoDto.Resp getUserInfo(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId).orElseThrow(NotFoundException::new);
        return converter.convertToDto(userInfo);
    }

    @Transactional
    public void createUserInfo(UserInfoDto.Req dto) {
        var account = accountRepository.findCurrent();
        var userInfo = new UserInfo().setAccount(account);
        converter.convertToEntity(userInfo, dto);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void updateUserInfo(UserInfoDto.Req dto) {
        var userInfo = userInfoRepository.findCurrent();
        converter.convertToEntity(userInfo, dto);
    }
}
