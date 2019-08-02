package app.config;

import app.common.OperationContext;
import app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final AccountService accountService;
    private final Set<String> openUri = Set.of("/api/register", "/api/login");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!openUri.contains(req.getRequestURI()) && !req.getMethod().equals("OPTIONS")) {
            var tokenCookie = req.getCookies() != null ?
                    Arrays.stream(req.getCookies()).filter(cook -> cook.getName().equals(AccountService.AUTH_TOKEN_NAME)).findFirst().orElse(null) :
                    null;

            accountService.authenticate(tokenCookie);

            // Обновляем куки
            Cookie newCookie = accountService.createUserCookie(OperationContext.getAccountId());
            res.addCookie(newCookie);
        }

        chain.doFilter(request, response);
    }
}
