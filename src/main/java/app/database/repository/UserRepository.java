package app.database.repository;

import app.common.OperationContext;
import app.common.SMessageSource;
import app.common.exception.ServiceException;
import app.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findCurrent() {
        return findById(OperationContext.getAccountId()).orElseThrow(() -> {
            // should never happen
            String message = SMessageSource.get("account.not_id");
            throw new ServiceException(message);
        });
    }
}
