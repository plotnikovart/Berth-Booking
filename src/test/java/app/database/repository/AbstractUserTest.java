package app.database.repository;

import app.ApplicationTest;
import app.database.entity.Account;
import app.database.entity.User;
import app.web.dto.AccountDto;
import app.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AbstractUserTest extends ApplicationTest {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected AccountRepository accountRepository;

    protected User user;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
        if (TestTransaction.isActive()) {
            commitAndStartNewTransaction();
        }

        var accountDto = new AccountDto();
        accountDto.setEmail("artpl98@mail.ru");
        accountDto.setPassword("pass");

        var account = new Account(accountDto);
        accountRepository.save(account);

        var userDto = new UserDto();
        userDto.setFirstName("Артем");
        userDto.setLastName("Плотников");
        userDto.setPhCode("164");
        userDto.setPhNumber("1238467");

        user = new User(account, userDto);
        userRepository.save(user);
    }
}
