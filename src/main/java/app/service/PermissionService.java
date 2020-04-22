package app.service;

import app.common.EntityWithOwner;
import app.common.SMessageSource;
import app.config.exception.impl.AccessException;
import app.config.security.OperationContext;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public void check(EntityWithOwner entityWithOwner) throws AccessException {
        long currentId = OperationContext.accountId();
        long ownerId = entityWithOwner.getOwnerId();

        if (currentId != ownerId) {
            // todo проверка на администратора
            String message = SMessageSource.get("access.forbidden");
            throw new AccessException(message);
        }
    }
}
