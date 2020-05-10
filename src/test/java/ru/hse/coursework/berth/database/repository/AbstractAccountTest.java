package ru.hse.coursework.berth.database.repository;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.ApplicationTest;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.UserInfo;
import ru.hse.coursework.berth.database.entity.enums.AccountRole;
import ru.hse.coursework.berth.service.account.AccountService;

public class AbstractAccountTest extends ApplicationTest {

    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected UserInfoRepository userInfoRepository;

    protected Account user1Account;
    protected Account user2Account;
    protected Account moderatorAccount;

    protected UserInfo user1Info;
    protected UserInfo user2Info;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();

        user1Account = accountService.createGoogleAccount("artpl98@gmail.com", AccountRole.USER);
        user2Account = accountService.createGoogleAccount("artpl99@gmail.com", AccountRole.USER);
        moderatorAccount = accountService.createEmailAccount("artpl98@mail.ru", new byte[]{1, 2}, new byte[]{1, 2}, AccountRole.MODERATOR);

        user1Info = userInfoRepository.findById(user1Account.getId()).orElseThrow();
        user2Info = userInfoRepository.findById(user2Account.getId()).orElseThrow();
    }
}
