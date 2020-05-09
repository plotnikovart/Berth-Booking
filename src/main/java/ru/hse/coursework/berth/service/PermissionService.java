package ru.hse.coursework.berth.service;

import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.common.EntityWithOwner;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.security.OperationContext;

@Service
public class PermissionService {

    public void check(EntityWithOwner entityWithOwner) throws AccessException {
        long currentId = OperationContext.accountId();
        long ownerId = entityWithOwner.getOwnerId();

        if (currentId != ownerId) {
            // todo проверка на администратора
            String message = SMessageSource.message("access.forbidden");
            throw new AccessException(message);
        }
    }
}
