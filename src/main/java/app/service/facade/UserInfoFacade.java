package app.service.facade;

import app.common.exception.NotFoundException;
import app.database.entity.UserInfo;
import app.database.repository.AccountRepository;
import app.database.repository.UserInfoRepository;
import app.web.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserInfoFacade {

    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public UserInfoDto.WithId getUserInfo(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId).orElseThrow(NotFoundException::new);
        return userInfo.getDto();
    }

    @Transactional
    public void createUserInfo(UserInfoDto userInfoDto) {
        var account = accountRepository.findCurrent();
        var userInfo = new UserInfo(account, userInfoDto);
        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void updateUserInfo(UserInfoDto userInfoDto) {
        var userInfo = userInfoRepository.findCurrent();
        userInfo.setDto(userInfoDto);
    }
}
