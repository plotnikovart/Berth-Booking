package app.service.account;

import app.common.PasswordUtils;
import app.common.SMessageSource;
import app.config.exception.impl.UnauthorizedException;
import app.database.entity.Account;
import app.database.entity.enums.AccountKind;
import app.database.entity.enums.AccountRole;
import app.database.repository.AccountRepository;
import app.service.account.dto.AuthToken;
import app.service.account.dto.EmailCredential;
import app.service.account.dto.GoogleCredential;
import app.service.account.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AccountLoginService {

    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final GoogleAuthClient googleAuthClient;


    public AuthToken loginEmail(EmailCredential emailCredential, String deviceId) throws UnauthorizedException {
        var accountOpt = accountRepository.findByEmail(emailCredential.getEmail());

        if (accountOpt.isEmpty()) {
            String message = SMessageSource.get("account.not_found");
            throw new UnauthorizedException(message);
        }

        Account account = accountOpt.get();
        if (!PasswordUtils.checkPassword(emailCredential.getPassword(), account.getPasswordHash(), account.getSalt())) {
            String message = SMessageSource.get("account.not_found");
            throw new UnauthorizedException(message);
        }

        return tokenService.createToken(account.getId(), deviceId);
    }


    public AuthToken loginGoogle(GoogleCredential googleCredential, String deviceId) throws UnauthorizedException {
        GoogleUserInfo userInfo = googleAuthClient.authenticate(googleCredential.getCode(), googleCredential.getRedirectUri());
        Optional<Account> accountOpt = accountRepository.findByGoogleMail(userInfo.getGmail());

        Account account = accountOpt.orElseGet(() -> createAccount(userInfo));
        return tokenService.createToken(account.getId(), deviceId);
    }

    private Account createAccount(GoogleUserInfo googleUserInfo) {
        var account = new Account()
                .setKind(AccountKind.GOOGLE)
                .setGoogleMail(googleUserInfo.getGmail())
                .setRoles(Set.of(AccountRole.USER));

        // todo user_info
        return accountRepository.saveAndFlush(account);
    }
}
