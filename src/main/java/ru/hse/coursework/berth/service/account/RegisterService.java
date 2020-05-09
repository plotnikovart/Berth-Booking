package ru.hse.coursework.berth.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.PasswordUtils;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;
import ru.hse.coursework.berth.database.entity.AccountConfirmation;
import ru.hse.coursework.berth.database.repository.AccountConfirmationRepository;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.service.EmailService;
import ru.hse.coursework.berth.service.account.dto.EmailCredential;

import java.time.LocalDateTime;
import java.util.UUID;

import static ru.hse.coursework.berth.database.entity.enums.AccountRole.USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final static int CONFIRM_MINUTES = 60;

    @Value("${front.url}")
    private String frontUrl;

    private final AccountRepository accountRepository;
    private final AccountConfirmationRepository accountConfirmationRepository;
    private final EmailService emailService;
    private final AccountService accountService;


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

            accountService.createEmailAccount(conf.getEmail(), conf.getPasswordHash(), conf.getSalt(), USER);
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
            String message = SMessageSource.message("account.already_exist", email);
            throw new ServiceException(message);
        }
    }


    private void checkPasswordComplexity(String password) {
        // todo
    }
}
