package app.controller;

import app.database.model.User;
import app.service.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthorizationController {

    private static final Logger LOGGER = Logger.getRootLogger();
    private final AuthorizationService authorizationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(User user, @RequestPart(required = false) MultipartFile photoFile) throws IOException {
        if (photoFile != null) {
            user.setPhoto(photoFile.getBytes());
        }
        authorizationService.register(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        var tokenOpt = authorizationService.login(loginRequest.getLogin(), loginRequest.getPassword());
        if (tokenOpt.isPresent()) {
            var cookie = new Cookie(AuthorizationService.AUTH_TOKEN, tokenOpt.get());
            cookie.setMaxAge(AuthorizationService.TOKEN_MAX_AGE);
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);

            return ResponseEntity.ok(new LoginResponse(tokenOpt.get()));    // todo разобраться с cookie
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();  // todo разобраться с cookie
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = AuthorizationService.AUTH_TOKEN) String token) {
        // authorizationService.logout(token);     // todo что-то сделать с этим
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Data
    private static class LoginRequest {
        private String login;
        private String password;
    }

    @Data
    @AllArgsConstructor
    private static class LoginResponse {
        private String token;
    }
}
