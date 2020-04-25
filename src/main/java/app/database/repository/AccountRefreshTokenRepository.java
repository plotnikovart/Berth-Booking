package app.database.repository;

import app.database.entity.AccountRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRefreshTokenRepository extends JpaRepository<AccountRefreshToken, UUID> {
}
