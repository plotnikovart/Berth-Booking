package app.service;

import app.database.entity.User;
import app.database.repository.AccountRepository;
import app.database.repository.UserRepository;
import app.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createUser(UserDto userDto) {
        var account = accountRepository.findCurrent();
        var user = new User(account, userDto);
        user = userRepository.save(user);
        return user.getAccountId();
    }

    @Transactional
    public Long updateUser(UserDto userDto) {
        var user = userRepository.findCurrent();
        user.setDto(userDto);
        return user.getAccountId();
    }
}
