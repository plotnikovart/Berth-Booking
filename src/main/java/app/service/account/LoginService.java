package app.service.account;

import app.common.PasswordUtils;
import app.common.SMessageSource;
import app.config.exception.impl.NotFoundException;
import app.config.exception.impl.UnauthorizedException;
import app.database.entity.Account;
import app.database.entity.UserInfo;
import app.database.repository.AccountRepository;
import app.database.repository.UserInfoRepository;
import app.service.account.dto.AuthToken;
import app.service.account.dto.EmailCredential;
import app.service.account.dto.GoogleCredential;
import app.service.account.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static app.database.entity.enums.AccountRole.USER;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final GoogleAuthClient googleAuthClient;
    private final UserInfoRepository userInfoRepository;
    private final AccountService accountService;


    public AuthToken loginEmail(EmailCredential emailCredential, String deviceId) throws UnauthorizedException {
        var accountOpt = accountRepository.findByEmail(emailCredential.getEmail());

        if (accountOpt.isEmpty()) {
            String message = SMessageSource.message("account.not_found");
            throw new UnauthorizedException(message);
        }

        Account account = accountOpt.get();
        if (!PasswordUtils.checkPassword(emailCredential.getPassword(), account.getPasswordHash(), account.getSalt())) {
            String message = SMessageSource.message("account.not_found");
            throw new UnauthorizedException(message);
        }

        return tokenService.createToken(account.getId(), deviceId);
    }


    @Transactional
    public AuthToken loginGoogle(GoogleCredential googleCredential, String deviceId) throws UnauthorizedException {
        GoogleUserInfo googleUserInfo = googleAuthClient.authenticate(googleCredential.getCode(), googleCredential.getRedirectUri());
        Optional<Account> accountOpt = accountRepository.findByGoogleMail(googleUserInfo.getGmail());

        Account account = accountOpt.orElseGet(() -> accountService.createGoogleAccount(googleUserInfo.getGmail(), USER));
        actualizeUserInfo(account, googleUserInfo);
        return tokenService.createToken(account.getId(), deviceId);
    }

    private UserInfo actualizeUserInfo(Account account, GoogleUserInfo googleUserInfo) {
        UserInfo userInfo = userInfoRepository.findById(account.getId()).orElseThrow(NotFoundException::new);

        return userInfo
                .setFirstName(googleUserInfo.getFirstName())
                .setLastName(googleUserInfo.getLastName())
                .setPhotoLink(googleUserInfo.getPhotoLink());
    }
}
