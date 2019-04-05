package app.controller;

import app.database.model.User;
import app.service.AuthorizationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Void> register(User user, @RequestPart(required = false) MultipartFile photoFile) throws IOException {
        user.setPhoto(photoFile.getBytes());
        authorizationService.register(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var tokenOpt = authorizationService.login(loginRequest.getLogin(), loginRequest.getPassword());
        if (tokenOpt.isPresent()) {
            var cookie = new Cookie(AuthorizationService.AUTH_TOKEN, tokenOpt.get());
            response.addCookie(cookie);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
}
