package app.service;

import app.common.OperationContext;
import app.common.ServiceException;
import app.common.ValidationUtils;
import app.database.dao.UserDao;
import app.database.model.Role;
import app.database.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorizationService {

    public final static String AUTH_TOKEN = "auth_token";

    @Value("${secretKey}")
    private String secretKey;

    private UserDao userDao;
    private OperationContext operationContext;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setOperationContext(OperationContext operationContext) {
        this.operationContext = operationContext;
    }

    @Transactional
    public void register(User user) {
        user.setId(null);
        user.setRoles(Set.of(Role.USER));
        ValidationUtils.validateEntity(user);
        try {
            userDao.save(user);
        } catch (Exception e) {
            throw new ServiceException("Ошибка при регистрации пользователя. Пользователь с данным логином/номером телефона уже существует");
        }
    }

    public Optional<String> login(String email, String password) {
        var userOpt = userDao.findByEmail(email);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            if (user.getPasswordHash().equals(User.createPasswordHash(password, user.getSalt()))) {
                var token = createToken(user);
                return Optional.of(token);
            }
        }

        return Optional.empty();
    }

    public boolean authenticate(String token) {
        return validateToken(token);
    }

    private String createToken(User user) {
        var claims = Jwts.claims().setSubject(user.getEmail());

        var now = new Date();
        var validityMillis = 30L * 24L * 60L * 60L * 1000L;    // 30 days
        var validity = new Date(now.getTime() + validityMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().after(new Date())) {
                var login = claims.getBody().getSubject();
                operationContext.setUserLogin(login);
                return true;
            }
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
