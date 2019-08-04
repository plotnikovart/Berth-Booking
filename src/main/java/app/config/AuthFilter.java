package app.config;

import app.common.OperationContext;
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

    private final AccountService accountService;
    private final Set<String> openUri = Set.of("/api/register", "/api/login");

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (!openUri.contains(req.getRequestURI()) && !req.getMethod().equals("OPTIONS")) {
            var tokenCookie = req.getCookies() != null ?
                    Arrays.stream(req.getCookies()).filter(cook -> cook.getName().equals(AccountService.AUTH_TOKEN_NAME)).findFirst().orElse(null) :
                    null;

            accountService.authenticate(tokenCookie);

            // Обновляем куки
            Cookie newCookie = accountService.createUserCookie(OperationContext.getAccountId());
            res.addCookie(newCookie);
        }

        chain.doFilter(req, res);
    }
}
