package app.web;

import app.common.OperationContext;
import app.common.ValidationUtils;
import app.service.facade.UserInfoFacade;
import app.web.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user_info")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoFacade userInfoFacade;

    @GetMapping
    public UserInfoDto.WithId getCurrentUserInfo() {
        Long userId = OperationContext.getAccountId();
        return userInfoFacade.getUserInfo(userId);
    }

    @GetMapping("/{id}")
    public UserInfoDto.WithId getUserInfo(@PathVariable Long id) {
        return userInfoFacade.getUserInfo(id);
    }

    @PostMapping
    public void createUserInfo(@RequestBody UserInfoDto userInfoDto) {
        ValidationUtils.validateEntity(userInfoDto);
        userInfoFacade.createUserInfo(userInfoDto);
    }

    @PutMapping
    public void updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        ValidationUtils.validateEntity(userInfoDto);
        userInfoFacade.updateUserInfo(userInfoDto);
    }
}
