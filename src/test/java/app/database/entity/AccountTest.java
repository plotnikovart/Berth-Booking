package app.database.entity;

import app.web.dto.AccountDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void testPassword() {
        String password = "pass";

        var accountDto = new AccountDto();
        accountDto.setPassword(password);
        accountDto.setEmail("email");

        var account = new Account(accountDto);
        Assertions.assertTrue(account.checkPassword(password));
        Assertions.assertFalse(account.checkPassword("1"));
    }
}