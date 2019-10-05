package app.database.repository;

import app.ApplicationTest;
import app.common.OperationContext;
import app.database.entity.Account;
import app.database.entity.UserInfo;
import app.service.converters.impl.UserInfoConverter;
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
    @Autowired
    protected UserInfoConverter userInfoConverter;

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

        var userDto = new UserInfoDto.Req();
        userDto.setFirstName("Артем");
        userDto.setLastName("Плотников");
        userDto.setPhCode("164");
        userDto.setPhNumber("1238467");

        userInfo = userInfoConverter.toEntity(userDto).setAccount(account);
        userInfoRepository.save(userInfo);

        OperationContext.setAccountId(userInfo.getAccountId());
    }
}
