package ru.hse.coursework.berth.websocket;

import de.jkeylockmanager.manager.KeyLockManager;
import de.jkeylockmanager.manager.implementation.lockstripe.StripedKeyLockManager;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class SocketSessionManager {

    private final Map<Long, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final KeyLockManager lockManager = new StripedKeyLockManager(1, TimeUnit.MINUTES, 1000);


    public void addSession(WebSocketSession session) {
        Long accountId = extractAccountId(session);

        lockManager.executeLocked(accountId, () -> {
            Set<WebSocketSession> accountSessions = sessions.computeIfAbsent(accountId, a -> new HashSet<>());
            accountSessions.add(session);
        });
    }

    public void removeSession(WebSocketSession session) {
        Long accountId = extractAccountId(session);

        lockManager.executeLocked(accountId, () -> {
            Set<WebSocketSession> accountSessions = sessions.computeIfAbsent(accountId, a -> new HashSet<>());
            if (accountSessions == null) {
                return;
            }

            accountSessions.remove(session);

            if (accountSessions.isEmpty()) {
                sessions.remove(accountId);
            }
        });
    }

    public Set<WebSocketSession> getSessionsByAccount(Long accountId) {
        return sessions.getOrDefault(accountId, new HashSet<>());
    }


    public Long extractAccountId(WebSocketSession session) {
        String principal = session.getPrincipal().getName();
        return Long.valueOf(principal);
    }
}
