package app.service.account;

import app.common.PasswordUtils;
import app.common.SMessageSource;
import app.config.exception.impl.UnauthorizedException;
import app.database.entity.Account;
import app.database.repository.AccountRepository;
import app.service.account.dto.AuthToken;
import app.service.account.dto.EmailCredential;
import app.service.account.dto.GoogleCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountLoginService {

    private final AccountRepository accountRepository;
    private final TokenService tokenService;


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
        return tokenService.createToken(1L, deviceId);  // todo
    }
}
