package app.database.repository;

import app.common.OperationContext;
import app.common.exception.ServiceException;
import app.database.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    default Account findCurrent() {
        return findById(OperationContext.getAccountId()).orElseThrow(ServiceException::new);
    }
}
