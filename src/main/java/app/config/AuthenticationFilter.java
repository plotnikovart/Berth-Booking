package app.config;

import app.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
@Order(2)
public class AuthenticationFilter implements Filter {

    private AuthorizationService authorizationService;
    private Set<String> openUri = Set.of("/api/register", "/api/login");

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (!openUri.contains(req.getRequestURI()) && !req.getMethod().equals("OPTIONS")) {
            var tokenCookie = req.getCookies() != null ? Arrays.stream(req.getCookies()).filter(cook -> cook.getName().equals(AuthorizationService.AUTH_TOKEN)).findFirst().orElse(null) : null;
            if (tokenCookie == null || !authorizationService.authenticate(tokenCookie.getValue())) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь неавторизован, выполните повторный вход");
            }
        }

        chain.doFilter(request, response);
    }
}
