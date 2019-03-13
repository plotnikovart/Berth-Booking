package app.controller;

import app.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
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
        if (!openUri.contains(req.getRequestURI())) {
            var tokenCookie = req.getCookies() != null ? Arrays.stream(req.getCookies()).filter(cook -> cook.getName().equals(AuthorizationService.AUTH_TOKEN)).findFirst().orElse(null) : null;
            if (tokenCookie == null || !authorizationService.authenticate(tokenCookie.getValue())) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь неавторизован, выполните повторный вход");
            }
        }

        chain.doFilter(request, response);
    }
}
