package app.config;

import app.common.HttpHelper;
import app.common.OperationContext;
import app.common.exception.UnauthorizedException;
import app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthFilter extends HttpFilter {

    private final Set<String> OPEN_URI = Set.of("/api/register", "/api/login", "/api/confirm");
    private final String LOGOUT_URI = "/api/logout";
    private final AccountService accountService;

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            if (!OPEN_URI.contains(req.getRequestURI())) {
                var tokenCookie = req.getCookies() != null ?
                        Arrays.stream(req.getCookies()).filter(cook -> cook.getName().equals(AccountService.AUTH_TOKEN_NAME)).findFirst().orElse(null) :
                        null;

                accountService.authenticate(tokenCookie);

                // Обновляем куки
                Cookie newCookie = LOGOUT_URI.equals(req.getRequestURI()) ?
                        accountService.createLogoutCookie() : accountService.createUserCookie(OperationContext.getAccountId());
                HttpHelper.addCookie(resp, newCookie);
            }

            chain.doFilter(req, resp);
        } catch (UnauthorizedException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
