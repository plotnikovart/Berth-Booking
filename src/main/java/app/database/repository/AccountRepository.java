package app.database.repository;

import app.config.security.OperationContext;
import app.database.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByGoogleMail(String gMail);


    default Account findCurrent() {
        return getOne(OperationContext.accountId());
    }
}
