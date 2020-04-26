package app.database.repository;

import app.common.SMessageSource;
import app.config.exception.impl.NotFoundException;
import app.config.security.OperationContext;
import app.database.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @Query("select u.photo from UserInfo u where u.changeDate >= ?1")
    List<UUID> findUpdatedPhotos(LocalDateTime since);

    default UserInfo findCurrent() {
        return findById(OperationContext.accountId()).orElseThrow(() -> {
            String message = SMessageSource.message("user_info.not_found");
            throw new NotFoundException(message);
        });
    }
}
