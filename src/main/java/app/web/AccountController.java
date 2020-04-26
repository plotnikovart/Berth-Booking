package app.web;

import app.service.account.AccountService;
import app.service.account.dto.AccountInfo;
import app.service.account.dto.UserInfoDto;
import app.web.dto.response.ObjectResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("info")
    public ObjectResp<AccountInfo> getAccountInfo() {
        AccountInfo info = accountService.getAccountInfo();
        return new ObjectResp<>(info);
    }

    @GetMapping("userInfo")
    public ObjectResp<UserInfoDto.Resp> getUserInfo() {
        UserInfoDto.Resp info = accountService.getUserInfo();
        return new ObjectResp<>(info);
    }

    @PutMapping("userInfo")
    public ObjectResp<UserInfoDto.Resp> updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        accountService.updateUserInfo(userInfoDto);
        UserInfoDto.Resp info = accountService.getUserInfo();
        return new ObjectResp<>(info);
    }
}
