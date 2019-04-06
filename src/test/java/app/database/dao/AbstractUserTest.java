package app.database.dao;

import app.ApplicationTest;
import app.database.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractUserTest extends ApplicationTest {

    @Autowired
    UserDao userDao;
    User user;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();

        user = new User();
        user.setEmail("artpl98@mail.ru");
        user.setPassword("123");
        user.setPhoneNum("2131122");
        userDao.save(user);
    }
}
