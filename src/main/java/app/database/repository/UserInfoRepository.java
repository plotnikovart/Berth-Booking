package app.database.repository;

import app.common.OperationContext;
import app.common.SMessageSource;
import app.common.exception.NotFoundException;
import app.database.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    default UserInfo findCurrent() {
        return findById(OperationContext.getAccountId()).orElseThrow(() -> {
            String message = SMessageSource.get("user_info.not_found");
            throw new NotFoundException(message);
        });
    }
}
