package app.service.account;

import app.common.DateHelper;
import app.config.exception.impl.AccessException;
import app.config.exception.impl.ServiceException;
import app.config.exception.impl.UnauthorizedException;
import app.service.account.dto.AuthToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;

import static java.util.Optional.ofNullable;

@Log4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String ACCOUNT_ID_CLAIM = "account_id";

    private static final long ACCESS_TOKEN_EXP = 2L * 3600 * 1000;         // 2 hour in milliseconds
    private static final long REFRESH_TOKEN_EXP = 30L * 24 * 3600 * 1000;  // 30 days in milliseconds


    @Value("${auth.secretKey}")
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
                    .setExpiresIn(ACCESS_TOKEN_EXP);
        } catch (Exception e) {
            log.error(e);
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
        // todo
        return -1L;
    }

    private String createRefreshToken(Long accountId, String deviceId) {
        // todo
        return "";
    }
}
