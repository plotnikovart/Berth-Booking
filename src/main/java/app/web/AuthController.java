package app.web;

import app.common.ValidationUtils;
import app.service.EmailService;
import app.service.account.AccountLoginService;
import app.service.account.AccountRegisterService;
import app.service.account.TokenService;
import app.service.account.dto.AuthToken;
import app.service.account.dto.EmailCredential;
import app.service.account.dto.GoogleCredential;
import app.service.account.dto.RefreshTokenReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;
    private final AccountLoginService accountLoginService;
    private final AccountRegisterService accountRegisterService;

    @PostMapping("/registration")
    public void register(@RequestBody EmailCredential emailCredential) {
        ValidationUtils.validateEntity(emailCredential);
        accountRegisterService.register(emailCredential);
    }

    @GetMapping("/registration/confirm")
    public void confirm(@RequestParam(name = EmailService.CONFIRM_CODE_PARAM) String code,
                        @RequestParam(name = EmailService.EMAIL_PARAM) String email,
                        HttpServletResponse resp) throws IOException {
        String redirectUrl = accountRegisterService.confirmAccount(code, email);
        resp.sendRedirect(redirectUrl);
    }

    @PostMapping("/login/email")
    public AuthToken login(@RequestBody EmailCredential emailCredential) {
        ValidationUtils.validateEntity(emailCredential);
        return accountLoginService.loginEmail(emailCredential, "");
    }

    @PostMapping("/login/google")
    public AuthToken loginGoogle(@RequestBody GoogleCredential googleCredential) {
        ValidationUtils.validateEntity(googleCredential);
        return accountLoginService.loginGoogle(googleCredential, "");
    }

    @PostMapping("/token/refresh")
    public AuthToken refreshToken(@RequestBody RefreshTokenReq req) {
        ValidationUtils.validateEntity(req);
        return tokenService.updateToken(req.getRefreshToken(), "");
    }
}
