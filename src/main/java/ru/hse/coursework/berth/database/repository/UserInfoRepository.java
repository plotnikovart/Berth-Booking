package ru.hse.coursework.berth.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.common.SMessageSource;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    default UserInfo findCurrent() {
        return findById(OperationContext.accountId()).orElseThrow(() -> {
            String message = SMessageSource.message("user_info.not_found");
            throw new NotFoundException(message);
        });
    }
}
