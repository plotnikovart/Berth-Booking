package ru.hse.coursework.berth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.coursework.berth.service.account.LoginService;
import ru.hse.coursework.berth.service.account.RegisterService;
import ru.hse.coursework.berth.service.account.TokenService;
import ru.hse.coursework.berth.service.account.dto.AuthToken;
import ru.hse.coursework.berth.service.account.dto.EmailCredential;
import ru.hse.coursework.berth.service.account.dto.RefreshTokenReq;
import ru.hse.coursework.berth.service.account.facebook.FacebookCredential;
import ru.hse.coursework.berth.service.account.google.GoogleCredential;
import ru.hse.coursework.berth.service.account.recovery.PasswordRecoveryService;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordChangeReq;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordRecoveryCheckReq;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordRecoveryReq;
import ru.hse.coursework.berth.web.dto.resp.EmptyResp;
import ru.hse.coursework.berth.web.dto.resp.ObjectResp;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.hse.coursework.berth.service.email.EmailService.CONFIRM_CODE_PARAM;
import static ru.hse.coursework.berth.service.email.EmailService.EMAIL_PARAM;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String DEVICE_ID_HEADER = "DeviceId";

    private final TokenService tokenService;
    private final LoginService loginService;
    private final RegisterService registerService;
    private final PasswordRecoveryService passwordRecoveryService;

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

    @PostMapping("/login/facebook")
    public ObjectResp<AuthToken> loginFacebook(@RequestBody FacebookCredential facebookCredential,
                                               @RequestHeader(name = DEVICE_ID_HEADER) String deviceId) {
        AuthToken token = loginService.loginFacebook(facebookCredential, deviceId);
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


    @PostMapping("/password/recovery")
    public EmptyResp recoveryPassword(@RequestBody PasswordRecoveryReq req) {
        passwordRecoveryService.recoveryPassword(req.getEmail());
        return new EmptyResp();
    }

    @PostMapping("/password/recovery/check")
    public EmptyResp checkRecoveryPassword(@RequestBody PasswordRecoveryCheckReq req) {
        passwordRecoveryService.checkRecoveryValidity(req.getEmail(), req.getRecoveryCode());
        return new EmptyResp();
    }

    @PostMapping("/password/change")
    public EmptyResp changePassword(@RequestBody PasswordChangeReq req) {
        passwordRecoveryService.changePassword(req);
        return new EmptyResp();
    }
}
