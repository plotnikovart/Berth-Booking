package ru.hse.coursework.berth.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.ApplicationTest;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.enums.AccountKind;
import ru.hse.coursework.berth.database.entity.enums.AccountRole;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.service.account.TokenService;
import ru.hse.coursework.berth.service.account.dto.AccountInfo;
import ru.hse.coursework.berth.service.account.dto.AuthToken;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.account.facebook.FacebookCredential;
import ru.hse.coursework.berth.service.account.facebook.FacebookUserInfo;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

class AuthControllerTest extends ApplicationTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthController authController;
    @Autowired
    AccountController accountController;
    @Autowired
    TokenService tokenService;

    private final FacebookUserInfo facebookUserInfo = new FacebookUserInfo()
            .setId(123L)
            .setEmail("facebook@mail.ru")
            .setFirstName("Artem")
            .setLastName("Plotnikov")
            .setPhotoUri("https://s.hi-news.ru/wp-content/uploads/2020/06/vegan_meat_health_image_two-750x499.jpg");

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
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
}