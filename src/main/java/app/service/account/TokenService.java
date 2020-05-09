package app.service.account;

import app.common.DateHelper;
import app.config.exception.impl.AccessException;
import app.config.exception.impl.ServiceException;
import app.config.exception.impl.UnauthorizedException;
import app.database.entity.Account;
import app.database.entity.AccountRefreshToken;
import app.database.repository.AccountRefreshTokenRepository;
import app.service.account.dto.AuthToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String ACCOUNT_ID_CLAIM = "account_id";
    private static final int CODE_LENGTH = 16;

    private static final long ACCESS_TOKEN_EXP = 2L * 3600 * 1000;         // 2 hour in milliseconds
    private static final long REFRESH_TOKEN_EXP = 30L * 24 * 3600 * 1000;  // 30 days in milliseconds

    private final AccountRefreshTokenRepository refreshTokenRepository;
    private final EntityManager em;

    @Value("${auth.secret_key}")
    private String secretKey;


    public AuthToken createToken(Long accountId, String deviceId) {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            var issueDate = LocalDateTime.now();
            var expireDate = issueDate.plusSeconds(ACCESS_TOKEN_EXP / 1000);

            String accessToken = JWT.create()
                    .withClaim(ACCOUNT_ID_CLAIM, accountId)
                    .withIssuedAt(DateHelper.convertToDate(issueDate))
                    .withExpiresAt(DateHelper.convertToDate(expireDate))
                    .sign(algorithm);

            String refreshToken = createRefreshToken(accountId, deviceId);

            return new AuthToken()
                    .setAccessToken(accessToken)
                    .setRefreshToken(refreshToken)
                    .setAccessExpiresIn(ACCESS_TOKEN_EXP)
                    .setRefreshExpiresIn(REFRESH_TOKEN_EXP);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e);
        }
    }


    public Long verifyAccessToken(String token) throws UnauthorizedException {
        try {
            var algorithm = Algorithm.HMAC256(secretKey);
            var verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(token);

            if (jwt.getExpiresAt().before(new Date())) {
                throw new AccessException();
            }

            var accountId = ofNullable(jwt.getClaim(ACCOUNT_ID_CLAIM).asLong())
                    .orElseThrow(AccessException::new);

            return accountId;
        } catch (UnsupportedEncodingException | JWTVerificationException ex) {
            throw new UnauthorizedException();
        }
    }


    public AuthToken updateToken(String refreshToken, String deviceId) {
        Long accountId = verifyRefreshToken(refreshToken, deviceId);
        return createToken(accountId, deviceId);
    }


    private Long verifyRefreshToken(String refreshToken, String deviceId) {
        if (refreshToken.length() != 36 + CODE_LENGTH) {
            throw new UnauthorizedException();
        }

        var id = UUID.fromString(refreshToken.substring(0, 36));
        var code = refreshToken.substring(36);

        AccountRefreshToken realToken = refreshTokenRepository.findById(id).orElseThrow(UnauthorizedException::new);

        if (realToken.getCode().equals(code)
                && realToken.getDeviceId().equals(deviceId)
                && realToken.getExpiresAt().isAfter(LocalDateTime.now())
                && !realToken.getUsed()
        ) {
            realToken.setUsed(true);
            refreshTokenRepository.save(realToken);
            return realToken.getAccount().getId();
        }

        throw new UnauthorizedException();
    }

    private String createRefreshToken(Long accountId, String deviceId) {
        var refreshToken = new AccountRefreshToken()
                .setId(UUID.randomUUID())
                .setCode(RandomStringUtils.random(CODE_LENGTH, true, true))
                .setDeviceId(deviceId)
                .setExpiresAt(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXP / 1000))
                .setAccount(em.getReference(Account.class, accountId));

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getId().toString() + refreshToken.getCode();
    }
}
