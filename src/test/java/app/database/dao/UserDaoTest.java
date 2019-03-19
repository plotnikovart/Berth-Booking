package app.database.dao;

import app.ApplicationTest;
import app.database.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDaoTest extends ApplicationTest {

    @Autowired
    private UserDao userDao;

    @Test
    void testPassword() {
        var userEmail = "artpl98@mail.ru";
        var userPassword = "pass";

        var user = new User();
        user.setPhoneNum("89153148028");
        user.setEmail(userEmail);
        user.setPassword(userPassword);
        userDao.save(user);

        var userFromDB = userDao.findByEmail(userEmail).orElseThrow();
        assertEquals(User.createPasswordHash(userPassword, userFromDB.getSalt()), userFromDB.getPasswordHash());
    }

}