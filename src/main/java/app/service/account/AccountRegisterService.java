package app.service.account;

import app.common.PasswordUtils;
import app.common.SMessageSource;
import app.config.exception.impl.NotFoundException;
import app.config.exception.impl.ServiceException;
import app.database.entity.Account;
import app.database.entity.AccountConfirmation;
import app.database.entity.UserInfo;
import app.database.entity.enums.AccountKind;
import app.database.entity.enums.AccountRole;
import app.database.repository.AccountConfirmationRepository;
import app.database.repository.AccountRepository;
import app.database.repository.UserInfoRepository;
import app.service.EmailService;
import app.service.account.dto.EmailCredential;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountRegisterService {

    private final static int CONFIRM_MINUTES = 60;

    @Value("${front.url}")
    private String frontUrl;

    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;
    private final AccountConfirmationRepository accountConfirmationRepository;
    private final EmailService emailService;


    public void register(EmailCredential emailCredential) {
        checkExistence(emailCredential.getEmail());
        checkPasswordComplexity(emailCredential.getPassword());

        Pair<byte[], byte[]> hashAndSalt = PasswordUtils.createHashAndSalt(emailCredential.getPassword());

        var confirmation = new AccountConfirmation()
                .setUuid(UUID.randomUUID())
                .setExpiredAt(LocalDateTime.now().plusMinutes(CONFIRM_MINUTES))
                .setEmail(emailCredential.getEmail())
                .setPasswordHash(hashAndSalt.getLeft())
                .setSalt(hashAndSalt.getRight());

        accountConfirmationRepository.save(confirmation);
        emailService.sendEmailConfirmation(emailCredential.getEmail(), confirmation.getUuid().toString());
    }

    /**
     * @return redirect uri
     */
    @Transactional
    public String confirmAccount(String code, String email) {
        try {
            var uuid = UUID.fromString(code);
            AccountConfirmation conf = accountConfirmationRepository.findById(uuid).orElseThrow(NotFoundException::new);

            if (!(conf.getEmail().equals(email)
                    && conf.getExpiredAt().isAfter(LocalDateTime.now())
                    && !conf.getConfirmed())) {
                throw new NotFoundException();
            }

            createAccount(conf);
            conf.setConfirmed(true);

            return frontUrl;    // todo
        } catch (Exception e) {
            log.error(e.getMessage());
            return frontUrl + "/error";
        }
    }


    private void checkExistence(String email) {
        var accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isPresent()) {
            String message = SMessageSource.get("account.already_exist", email);
            throw new ServiceException(message);
        }
    }


    private void checkPasswordComplexity(String password) {
        // todo
    }


    private Account createAccount(AccountConfirmation conf) {
        var account = new Account()
                .setKind(AccountKind.EMAIL)
                .setEmail(conf.getEmail())
                .setPasswordHash(conf.getPasswordHash())
                .setSalt(conf.getSalt())
                .setRoles(Set.of(AccountRole.USER));

        account = accountRepository.saveAndFlush(account);

        var userInfo = new UserInfo()
                .setAccount(account);

        userInfoRepository.save(userInfo);

        return account;
    }
}
