package app.database.repository;

import app.common.SMessageSource;
import app.config.exception.impl.NotFoundException;
import app.config.security.OperationContext;
import app.database.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    default UserInfo findCurrent() {
        return findById(OperationContext.accountId()).orElseThrow(() -> {
            String message = SMessageSource.get("user_info.not_found");
            throw new NotFoundException(message);
        });
    }
}
