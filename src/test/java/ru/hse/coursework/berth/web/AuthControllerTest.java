package ru.hse.coursework.berth.web;

import one.util.streamex.StreamEx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.ApplicationTest;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.enums.AccountKind;
import ru.hse.coursework.berth.database.entity.enums.AccountRole;
import ru.hse.coursework.berth.database.repository.AccountPasswordRecoveryRepository;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.service.account.AccountService;
import ru.hse.coursework.berth.service.account.TokenService;
import ru.hse.coursework.berth.service.account.dto.AccountInfo;
import ru.hse.coursework.berth.service.account.dto.AuthToken;
import ru.hse.coursework.berth.service.account.dto.EmailCredential;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.account.facebook.FacebookCredential;
import ru.hse.coursework.berth.service.account.facebook.FacebookUserInfo;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordChangeReq;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordRecoveryCheckReq;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordRecoveryReq;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AuthControllerTest extends ApplicationTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountPasswordRecoveryRepository accountPasswordRecoveryRepository;
    @Autowired
    AuthController authController;
    @Autowired
    AccountController accountController;
    @Autowired
    TokenService tokenService;
    @Autowired
    AccountService accountService;

    private final FacebookUserInfo facebookUserInfo = new FacebookUserInfo()
            .setId(123L)
            .setEmail("facebook@mail.ru")
            .setFirstName("Artem")
            .setLastName("Plotnikov")
            .setPicture(new FacebookUserInfo.Picture()
                    .setData(new FacebookUserInfo.Picture.Data()
                            .setHeight(320)
                            .setWidth(320)
                            .setUrl("https://s.hi-news.ru/wp-content/uploads/2020/06/vegan_meat_health_image_two-750x499.jpg")
                    )
            );

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        accountRepository.deleteAll();
        accountPasswordRecoveryRepository.deleteAll();

        Mockito.doReturn(facebookUserInfo).when(facebookAuthClientMock).authenticate(any(), any());
    }

    @Test
    void testFacebook() {
        var credential = new FacebookCredential()
                .setCode("code")
                .setRedirectUri("redirectUri");

        AuthToken authToken = authController.loginFacebook(credential, "deviceId").getData();
        Long accountId = tokenService.verifyAccessToken(authToken.getAccessToken());

        OperationContext.accountId(accountId);
        AccountInfo actualAccount = accountController.getAccountInfo().getData();
        UserInfoDto.Resp actualUserInfo = accountController.getUserInfo().getData();

        var expectedAccount = new AccountInfo()
                .setId(accountId)
                .setKind(AccountKind.FACEBOOK)
                .setEmail(facebookUserInfo.getEmail())
                .setRoles(List.of(AccountRole.USER))
                .setHasBerths(false)
                .setHasShips(false);

        var expectedUserInfo = new UserInfoDto.Resp()
                .setAccountId(accountId)
                .setLastName(facebookUserInfo.getLastName())
                .setFirstName(facebookUserInfo.getFirstName())
                .setPhCode(null)
                .setPhNumber(null)
                .setPhoto(actualUserInfo.getPhoto());

        Assertions.assertEquals(expectedAccount, actualAccount);
        Assertions.assertEquals(expectedUserInfo, actualUserInfo);
        Assertions.assertNotNull(expectedUserInfo.getPhoto());
    }

    @Test
    void testRecovery() {
        String email = "a@a.ru";

        accountService.createEmailAccount(email, new byte[]{1, 2}, new byte[]{1, 2});

        authController.recoveryPassword(new PasswordRecoveryReq().setEmail(email));
        String code = StreamEx.of(accountPasswordRecoveryRepository.findAll()).map(it -> it.getPk().getCode()).findFirst().orElseThrow();

        authController.checkRecoveryPassword(new PasswordRecoveryCheckReq().setEmail(email).setRecoveryCode(code));
        authController.changePassword(new PasswordChangeReq().setEmail(email).setRecoveryCode(code).setNewPassword("a"));

        authController.login(new EmailCredential().setEmail(email).setPassword("a"), "");
        Assertions.assertTrue(accountPasswordRecoveryRepository.findAll().isEmpty());
    }
}