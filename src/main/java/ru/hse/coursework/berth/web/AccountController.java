package ru.hse.coursework.berth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.account.AccountService;
import ru.hse.coursework.berth.service.account.dto.AccountInfo;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.berth.BerthCRUDService;
import ru.hse.coursework.berth.service.ship.ShipCRUDService;
import ru.hse.coursework.berth.web.dto.resp.ObjectResp;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ShipCRUDService shipCRUDService;
    private final BerthCRUDService berthCRUDService;

    @GetMapping("info")
    public ObjectResp<AccountInfo> getAccountInfo() {
        AccountInfo info = accountService.getAccountInfo()
                // небольшой костылек
                .setHasBerths(!berthCRUDService.get().isEmpty())
                .setHasShips(!shipCRUDService.get().isEmpty());
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
