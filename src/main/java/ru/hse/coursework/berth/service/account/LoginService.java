package ru.hse.coursework.berth.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.PasswordUtils;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.UnauthorizedException;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.UserInfo;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.service.account.dto.AuthToken;
import ru.hse.coursework.berth.service.account.dto.EmailCredential;
import ru.hse.coursework.berth.service.account.dto.GoogleCredential;
import ru.hse.coursework.berth.service.account.dto.GoogleUserInfo;

import java.util.Optional;

import static ru.hse.coursework.berth.database.entity.enums.AccountRole.USER;


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
