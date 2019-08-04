package app.web;

import app.common.ValidationUtils;
import app.service.AccountService;
import app.web.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("/login")
    public CookieResponse login(@RequestBody AccountDto accountDto, HttpServletResponse response) {
        ValidationUtils.validateEntity(accountDto);
        var cookie = accountService.login(accountDto);
        response.addCookie(cookie);

        return new CookieResponse(cookie.getName(), cookie.getValue());
    }

    @Getter
    @AllArgsConstructor
    public static class CookieResponse {        // todo убрать
        private String name;
        private String value;
    }
}
