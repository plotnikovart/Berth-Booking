package app.service;

import app.common.EntityWithOwner;
import app.common.OperationContext;
import app.common.SMessageSource;
import app.common.exception.AccessException;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public void checkPermission(EntityWithOwner entityWithOwner) throws AccessException {
        long currentId = OperationContext.getAccountId();
        long ownerId = entityWithOwner.getOwnerId();

        if (currentId != ownerId) {
            // todo проверка на администратора
            String message = SMessageSource.get("access.forbidden");
            throw new AccessException(message);
        }
    }
}
