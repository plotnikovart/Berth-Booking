package app.service;

import app.common.OperationContext;
import app.common.SMessageSource;
import app.common.exception.NotFoundException;
import app.common.exception.ServiceException;
import app.common.exception.UnauthorizedException;
import app.database.entity.Account;
import app.database.repository.AccountRepository;
import app.web.dto.AccountDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    public final static String CONFIRM_CODE = "code";
    public final static String AUTH_TOKEN_NAME = "auth_token";
    private final static Integer TOKEN_MAX_AGE = 5 * 24 * 60 * 60;    // 5 days in seconds

    @Value("${secretKey}")
    private String secretKey;
    @Value("${front.url}")
    private String frontUrl;

    private final Map<String, AccountDto> codeToAccountMap = new ConcurrentHashMap<>();

    private final AccountRepository accountRepository;
    private final EmailService emailService;


    @Transactional
    public void register(AccountDto accountDto) {
        checkExistence(accountDto.getEmail());
        checkPasswordComplexity(accountDto.getPassword());

        String code = RandomStringUtils.random(20, true, true);
        codeToAccountMap.put(code, accountDto);

        emailService.sendEmailConfirmation(accountDto.getEmail(), code);
    }

    @Transactional
    public String confirmAccount(String code) {
        try {
            var accountDto = Optional.ofNullable(codeToAccountMap.get(code)).orElseThrow(NotFoundException::new);
            codeToAccountMap.remove(code);

            var account = new Account(accountDto);
            accountRepository.save(account);

            return frontUrl + "/success";
        } catch (Exception e) {
            log.error(e.getMessage());
            return frontUrl + "/error";
        }
    }

    public Cookie login(AccountDto accountDto) throws UnauthorizedException {
        var accountOpt = accountRepository.findByEmail(accountDto.getEmail());

        if (accountOpt.isPresent()) {
            var account = accountOpt.get();
            if (account.checkPassword(accountDto.getPassword())) {
                return createUserCookie(account.getId());
            }
        }

        String message = SMessageSource.get("account.not_found");
        throw new UnauthorizedException(message);
    }

    public void authenticate(@Nullable Cookie cookie) throws UnauthorizedException {
        if (!(cookie != null && validateToken(cookie.getValue()))) {
            String message = SMessageSource.get("account.unauthorized");
            throw new UnauthorizedException(message);
        }
    }

    public Cookie createLogoutCookie() {
        var cookie = new Cookie(AUTH_TOKEN_NAME, "none");
        cookie.setMaxAge(-1);
        return cookie;
    }

    public Cookie createUserCookie(Long accountId) {
        var token = createToken(accountId);
        var cookie = new Cookie(AUTH_TOKEN_NAME, token);
        cookie.setMaxAge(TOKEN_MAX_AGE);
        return cookie;
    }

    private String createToken(Long accountId) {
        var claims = Jwts.claims().setSubject(accountId.toString());

        var now = new Date();
        var validity = new Date(now.getTime() + 1000L * TOKEN_MAX_AGE);

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
                OperationContext.setAccountId(Long.valueOf(login));
                return true;
            }

            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private void checkExistence(String email) {
        var accountOpt = accountRepository.findByEmail(email);
        if (accountOpt.isPresent()) {
            String message = SMessageSource.get("account.already_exist", email);
            throw new ServiceException(message);
        }
    }


    private void checkPasswordComplexity(String password) {
        // todo
    }

}
