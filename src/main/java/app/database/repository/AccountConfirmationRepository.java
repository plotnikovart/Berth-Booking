package app.database.repository;

import app.database.entity.AccountConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation, UUID> {
}
