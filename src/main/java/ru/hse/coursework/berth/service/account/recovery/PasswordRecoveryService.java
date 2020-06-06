package ru.hse.coursework.berth.service.account.recovery;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.common.PasswordUtils;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.AccountPasswordRecovery;
import ru.hse.coursework.berth.database.repository.AccountPasswordRecoveryRepository;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.service.account.recovery.dto.PasswordChangeReq;
import ru.hse.coursework.berth.service.email.EmailService;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    public static final int RECOVERY_CODE_LENGTH = 6;
    private static final int RECOVERY_CODE_EXPIRE = 60;  // minutes

    private final AccountRepository accountRepository;
    private final AccountPasswordRecoveryRepository accountPasswordRecoveryRepository;
    private final EmailService emailService;

    public void recoveryPassword(String email) {
        accountRepository.findByEmail(email).orElseThrow(NotFoundException::new);
        String code = generateRecoveryCode();

        var recovery = new AccountPasswordRecovery()
                .setPk(new AccountPasswordRecovery.PK()
                        .setCode(code)
                        .setEmail(email))
                .setExpiresAt(LocalDateTime.now().plusMinutes(RECOVERY_CODE_EXPIRE));

        accountPasswordRecoveryRepository.save(recovery);
        emailService.sendPasswordRecovery(email, code);
    }


    public void checkRecoveryValidity(String email, String code) {
        var pk = new AccountPasswordRecovery.PK()
                .setCode(code)
                .setEmail(email);

        AccountPasswordRecovery recovery = accountPasswordRecoveryRepository.findById(pk).orElseThrow(NotFoundException::new);
        if (LocalDateTime.now().isAfter(recovery.getExpiresAt())) {
            throw new NotFoundException();
        }
    }


    @Transactional
    public void changePassword(PasswordChangeReq dto) {
        checkRecoveryValidity(dto.getEmail(), dto.getRecoveryCode());
        Account account = accountRepository.findByEmail(dto.getEmail()).orElseThrow(NotFoundException::new);

        Pair<byte[], byte[]> hashAndSalt = PasswordUtils.createHashAndSalt(dto.getNewPassword());
        account
                .setPasswordHash(hashAndSalt.getLeft())
                .setSalt(hashAndSalt.getRight());

        var pk = new AccountPasswordRecovery.PK()
                .setCode(dto.getRecoveryCode())
                .setEmail(dto.getEmail());
        accountPasswordRecoveryRepository.deleteById(pk);
    }


    private String generateRecoveryCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < RECOVERY_CODE_LENGTH; i++) {
            int num = ThreadLocalRandom.current().nextInt(0, 10);
            code.append(num);
        }

        return code.toString();
    }
}
