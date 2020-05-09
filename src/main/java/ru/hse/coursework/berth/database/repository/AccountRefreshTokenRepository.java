package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.AccountRefreshToken;

import java.util.UUID;

@Repository
public interface AccountRefreshTokenRepository extends JpaRepository<AccountRefreshToken, UUID> {
}
