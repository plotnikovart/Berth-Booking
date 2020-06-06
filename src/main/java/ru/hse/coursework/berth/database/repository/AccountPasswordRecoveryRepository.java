package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.AccountPasswordRecovery;

@Repository
public interface AccountPasswordRecoveryRepository extends JpaRepository<AccountPasswordRecovery, AccountPasswordRecovery.PK> {
}
