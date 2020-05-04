package app.database.repository;


import app.ApplicationTest;
import app.database.entity.Account;
import app.database.entity.enums.AccountRole;
import app.service.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractAccountTest extends ApplicationTest {

    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccountService accountService;

    protected Account userAccount;
    protected Account moderatorAccount;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();

        userAccount = accountService.createGoogleAccount("artpl98@gmail.com", AccountRole.USER);
        moderatorAccount = accountService.createEmailAccount("artpl98@mail.ru", new byte[]{1, 2}, new byte[]{1, 2}, AccountRole.MODERATOR);
    }
}
