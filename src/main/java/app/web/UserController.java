package app.web;

import app.common.ValidationUtils;
import app.service.UserFacade;
import app.web.dto.UserDto;
import app.web.dto.response.IdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public IdResponse<Long> createUser(UserDto userDto) {
        ValidationUtils.validateEntity(userDto);
        Long userId = userFacade.createUser(userDto);
        return new IdResponse<>(userId);
    }

    @PutMapping
    public IdResponse<Long> updateUser(UserDto userDto) {
        ValidationUtils.validateEntity(userDto);
        Long userId = userFacade.updateUser(userDto);
        return new IdResponse<>(userId);
    }
}
