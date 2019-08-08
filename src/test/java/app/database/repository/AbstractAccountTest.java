package app.database.repository;

import app.ApplicationTest;
import app.common.OperationContext;
import app.database.entity.Account;
import app.database.entity.UserInfo;
import app.web.dto.AccountDto;
import app.web.dto.UserInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AbstractAccountTest extends ApplicationTest {

    @Autowired
    protected UserInfoRepository userInfoRepository;
    @Autowired
    protected AccountRepository accountRepository;

    protected Account account;
    protected UserInfo userInfo;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
        if (TestTransaction.isActive()) {
            commitAndStartNewTransaction();
        }

        var accountDto = new AccountDto();
        accountDto.setEmail("artpl98@mail.ru");
        accountDto.setPassword("pass");

        account = new Account(accountDto);
        accountRepository.save(account);

        var userDto = new UserInfoDto();
        userDto.setFirstName("Артем");
        userDto.setLastName("Плотников");
        userDto.setPhCode("164");
        userDto.setPhNumber("1238467");

        userInfo = new UserInfo(account, userDto);
        userInfoRepository.save(userInfo);

        OperationContext.setAccountId(userInfo.getAccountId());
    }
}
