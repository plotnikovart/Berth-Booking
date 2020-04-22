package app.config.security;

import app.config.exception.impl.UnauthorizedException;
import app.service.account.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter extends HttpFilter {

    private final static String AUTH_TOKEN_HEADER = "Authorization";
    private final static List<String> OPEN_URI = List.of(
            "/api/auth/.*",                 // authentication
            "/", "/berth", "/berth/.*"      // static
    );

    private final TokenService tokenService;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            if (!isOpen(req.getRequestURI())) {
                String token = extractToken(req);
                Long accountId = tokenService.verifyAccessToken(token);
                OperationContext.accountId(accountId);
            }

            chain.doFilter(req, resp);
        } catch (UnauthorizedException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } finally {
            OperationContext.clear();
        }
    }


    private boolean isOpen(String uri) {
        for (var openUri : OPEN_URI) {
            if (uri.matches(openUri)) {
                return true;
            }
        }

        return false;
    }


    private String extractToken(HttpServletRequest req) throws UnauthorizedException {
        String tokenValue = req.getHeader(AUTH_TOKEN_HEADER);

        if (tokenValue == null) {
            throw new UnauthorizedException();
        }

        return tokenValue.contains("Bearer ") ? StringUtils.substringAfterLast(tokenValue, "Bearer ") : tokenValue;
    }
}
