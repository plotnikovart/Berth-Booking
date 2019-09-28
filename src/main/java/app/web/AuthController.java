package app.web;

import app.common.HttpHelper;
import app.common.ValidationUtils;
import app.service.AccountService;
import app.web.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/register")
    public void register(@RequestBody AccountDto accountDto) {
        ValidationUtils.validateEntity(accountDto);
        accountService.register(accountDto);
    }

    @GetMapping("/confirm")
    public void confirm(@RequestParam(name = AccountService.CONFIRM_CODE) String code, HttpServletResponse resp) throws IOException {
        String redirectUrl = accountService.confirmAccount(code);
        resp.sendRedirect(redirectUrl);
    }

    @PostMapping("/login")
    public void login(@RequestBody AccountDto accountDto, HttpServletResponse response) {
        ValidationUtils.validateEntity(accountDto);
        var cookie = accountService.login(accountDto);
        HttpHelper.addCookie(response, cookie);
    }

    @PostMapping("/logout")
    public void logout() {
    }
}
