package ru.hse.coursework.berth.service.account;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import ru.hse.coursework.berth.service.account.facebook.FacebookAuthClient;
import ru.hse.coursework.berth.service.account.facebook.FacebookCredential;
import ru.hse.coursework.berth.service.account.facebook.FacebookUserInfo;
import ru.hse.coursework.berth.service.account.google.GoogleAuthClient;
import ru.hse.coursework.berth.service.account.google.GoogleCredential;
import ru.hse.coursework.berth.service.account.google.GoogleUserInfo;
import ru.hse.coursework.berth.service.file.FileStorageService;
import ru.hse.coursework.berth.service.file.dto.FileInfoDto;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import static ru.hse.coursework.berth.database.entity.enums.AccountRole.USER;


@Service
@RequiredArgsConstructor
public class LoginService {

    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final GoogleAuthClient googleAuthClient;
    private final UserInfoRepository userInfoRepository;
    private final AccountService accountService;
    private final FacebookAuthClient facebookAuthClient;
    private final FileStorageService fileStorageService;


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


    @Transactional
    public AuthToken loginFacebook(FacebookCredential facebookCredential, String deviceId) throws UnauthorizedException {
        FacebookUserInfo facebookUserInfo = facebookAuthClient.authenticate(facebookCredential.getCode(), facebookCredential.getRedirectUri());
        Optional<Account> accountOpt = accountRepository.findByFacebookId(facebookUserInfo.getId());

        Account account = accountOpt.orElseGet(() -> accountService.createFacebookAccount(facebookUserInfo.getId(), facebookUserInfo.getEmail(), USER));
        actualizeUserInfo(account, facebookUserInfo);
        return tokenService.createToken(account.getId(), deviceId);
    }


    private UserInfo actualizeUserInfo(Account account, GoogleUserInfo googleUserInfo) {
        UserInfo userInfo = userInfoRepository.findById(account.getId()).orElseThrow(NotFoundException::new);

        return userInfo
                .setFirstName(googleUserInfo.getFirstName())
                .setLastName(googleUserInfo.getLastName())
                .setPhotoExternal(linkToFileId(googleUserInfo.getPhotoLink()));
    }

    private UserInfo actualizeUserInfo(Account account, FacebookUserInfo facebookUserInfo) {
        account.setFacebookMail(facebookUserInfo.getEmail());
        UserInfo userInfo = userInfoRepository.findById(account.getId()).orElseThrow(NotFoundException::new);

        return userInfo
                .setFirstName(facebookUserInfo.getFirstName())
                .setLastName(facebookUserInfo.getLastName())
                .setPhotoExternal(linkToFileId(facebookUserInfo.getPicture().getData().getUrl()));
    }

    @SneakyThrows
    private UUID linkToFileId(String photoLink) {
        var url = new URL(photoLink);
        InputStream in = url.openConnection().getInputStream();
        FileInfoDto fileInfoDto = fileStorageService.saveFile(in.readAllBytes(), "userphoto.jpg");
        return fileInfoDto.getFileId();
    }
}
