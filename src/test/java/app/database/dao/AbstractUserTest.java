package app.database.dao;

import app.ApplicationTest;
import app.database.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TestTransaction;

public class AbstractUserTest extends ApplicationTest {

    @Autowired
    protected UserDao userDao;
    protected User user;

    @BeforeEach
    public void setUp() {
        userDao.deleteAll();
        if (TestTransaction.isActive()) {
            commitAndStartNewTransaction();
        }

        user = new User();
        user.setEmail("artpl98@mail.ru");
        user.setPassword("123");
        user.setPhoneNum("2131122");
        userDao.save(user);
    }
}
