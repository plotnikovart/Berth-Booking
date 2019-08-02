package app.database.repository;

import app.common.OperationContext;
import app.common.SMessageSource;
import app.common.exception.ServiceException;
import app.database.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    default Account findCurrent() {
        return findById(OperationContext.getAccountId()).orElseThrow(() -> {
            // should never happen
            String message = SMessageSource.get("account.not_id");
            throw new ServiceException(message);
        });
    }
}
