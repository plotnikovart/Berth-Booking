package app.service.account;

import app.config.exception.impl.NotFoundException;
import app.database.entity.Account;
import app.database.entity.UserInfo;
import app.database.entity.enums.AccountKind;
import app.database.entity.enums.AccountRole;
import app.database.repository.AccountRepository;
import app.database.repository.UserInfoRepository;
import app.service.account.dto.AccountInfo;
import app.service.account.dto.UserInfoDto;
import app.service.converters.impl.UserInfoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;

import static app.database.entity.enums.AccountRole.USER;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoConverter userInfoConverter;

    @Transactional(readOnly = true)
    public AccountInfo getAccountInfo() {
        Account a = accountRepository.findCurrent();
        String email = null;
        switch (a.getKind()) {
            case EMAIL:
                email = a.getEmail();
                break;
            case GOOGLE:
                email = a.getGoogleMail();
                break;
        }

        return new AccountInfo()
                .setId(a.getId())
                .setKind(a.getKind())
                .setRoles(new ArrayList<>(a.getRoles()))
                .setEmail(email);
    }

    @Transactional(readOnly = true)
    public UserInfoDto.Resp getUserInfo() {
        Account a = accountRepository.findCurrent();
        if (!a.getRoles().contains(USER)) {
            throw new NotFoundException();
        }

        UserInfo info = userInfoRepository.findCurrent();
        return userInfoConverter.toDto(info);
    }

    @Transactional
    public void updateUserInfo(UserInfoDto userInfoDto) {
        Account a = accountRepository.findCurrent();
        if (!a.getRoles().contains(USER)) {
            throw new NotFoundException();
        }

        UserInfo info = userInfoRepository.findCurrent();
        userInfoConverter.toEntity(info, userInfoDto);
    }


    Account createEmailAccount(String email, byte[] passwordHash, byte[] salt, AccountRole... roles) {
        var account = new Account()
                .setKind(AccountKind.EMAIL)
                .setEmail(email)
                .setPasswordHash(passwordHash)
                .setSalt(salt)
                .setRoles(Set.of(roles));

        account = accountRepository.saveAndFlush(account);

        if (account.getRoles().contains(USER)) {
            var userInfo = new UserInfo()
                    .setAccount(account);

            userInfoRepository.save(userInfo);
        }

        return account;
    }

    Account createGoogleAccount(String gmail, AccountRole... roles) {
        var account = new Account()
                .setKind(AccountKind.GOOGLE)
                .setGoogleMail(gmail)
                .setRoles(Set.of(roles));

        account = accountRepository.saveAndFlush(account);

        if (account.getRoles().contains(USER)) {
            var userInfo = new UserInfo()
                    .setAccount(account);

            userInfoRepository.save(userInfo);
        }

        return account;
    }
}
