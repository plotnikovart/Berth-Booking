package ru.hse.coursework.berth.config.security;

import lombok.RequiredArgsConstructor;
import org.apache.http.auth.BasicUserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.hse.coursework.berth.config.exception.impl.UnauthorizedException;
import ru.hse.coursework.berth.service.account.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class SocketHandshakeHandler extends DefaultHandshakeHandler {

    private static final String TOKEN_PARAM_NAME = "accessToken";

    private final TokenService tokenService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

        String token = ofNullable(servletRequest.getParameter(TOKEN_PARAM_NAME))
                .orElseThrow(UnauthorizedException::new);

        Long accountId = tokenService.verifyAccessToken(token);
        return new BasicUserPrincipal(accountId.toString());
    }
}
