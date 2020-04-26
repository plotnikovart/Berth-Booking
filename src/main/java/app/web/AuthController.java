package app.web;

import app.service.account.LoginService;
import app.service.account.RegisterService;
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

import static app.service.EmailService.CONFIRM_CODE_PARAM;
import static app.service.EmailService.EMAIL_PARAM;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String DEVICE_ID_HEADER = "DeviceId";

    private final TokenService tokenService;
    private final LoginService loginService;
    private final RegisterService registerService;

    @PostMapping("/registration")
    public EmptyResp register(@RequestBody EmailCredential emailCredential) {
        registerService.register(emailCredential);
        return new EmptyResp();
    }

    @GetMapping("/registration/confirm")
    public void confirm(@RequestParam(name = CONFIRM_CODE_PARAM) String code,
                        @RequestParam(name = EMAIL_PARAM) String email,
                        HttpServletResponse resp) throws IOException {
        String redirectUrl = registerService.confirmAccount(code, email);
        resp.sendRedirect(redirectUrl);
    }

    @PostMapping("/login/email")
    public ObjectResp<AuthToken> login(@RequestBody EmailCredential emailCredential,
                                       @RequestHeader(name = DEVICE_ID_HEADER) String deviceId) {
        AuthToken token = loginService.loginEmail(emailCredential, deviceId);
        return new ObjectResp<>(token);
    }

    @PostMapping("/login/google")
    public ObjectResp<AuthToken> loginGoogle(@RequestBody GoogleCredential googleCredential,
                                             @RequestHeader(name = DEVICE_ID_HEADER) String deviceId) {
        AuthToken token = loginService.loginGoogle(googleCredential, deviceId);
        return new ObjectResp<>(token);
    }

    @PostMapping("/logout")
    public EmptyResp logout() {
        return new EmptyResp();
    }

    @PostMapping("/token/refresh")
    public ObjectResp<AuthToken> refreshToken(@RequestBody RefreshTokenReq req,
                                              @RequestHeader(name = DEVICE_ID_HEADER) String deviceId) {
        AuthToken token = tokenService.updateToken(req.getRefreshToken(), deviceId);
        return new ObjectResp<>(token);
    }
}
