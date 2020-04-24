package app.web;

import app.service.EmailService;
import app.service.account.AccountLoginService;
import app.service.account.AccountRegisterService;
import app.service.account.TokenService;
import app.service.account.dto.AuthToken;
import app.service.account.dto.EmailCredential;
import app.service.account.dto.GoogleCredential;
import app.service.account.dto.RefreshTokenReq;
import app.web.dto.response.EmptyResp;
import app.web.dto.response.ObjectResp;
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
    public EmptyResp register(@RequestBody EmailCredential emailCredential) {
        accountRegisterService.register(emailCredential);
        return new EmptyResp();
    }

    @GetMapping("/registration/confirm")
    public EmptyResp confirm(@RequestParam(name = EmailService.CONFIRM_CODE_PARAM) String code,
                             @RequestParam(name = EmailService.EMAIL_PARAM) String email,
                             HttpServletResponse resp) throws IOException {
        String redirectUrl = accountRegisterService.confirmAccount(code, email);
        resp.sendRedirect(redirectUrl);
        return new EmptyResp();
    }

    @PostMapping("/login/email")
    public ObjectResp<AuthToken> login(@RequestBody EmailCredential emailCredential) {
        AuthToken token = accountLoginService.loginEmail(emailCredential, "");
        return new ObjectResp<>(token);
    }

    @PostMapping("/login/google")
    public ObjectResp<AuthToken> loginGoogle(@RequestBody GoogleCredential googleCredential) {
        AuthToken token = accountLoginService.loginGoogle(googleCredential, "");
        return new ObjectResp<>(token);
    }

    @PostMapping("/token/refresh")
    public ObjectResp<AuthToken> refreshToken(@RequestBody RefreshTokenReq req) {
        AuthToken token = tokenService.updateToken(req.getRefreshToken(), "");
        return new ObjectResp<>(token);
    }
}
