package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.AccountConfirmation;

import java.util.UUID;

@Repository
public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation, UUID> {
}
