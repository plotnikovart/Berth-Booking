package app.database.entity;

import app.config.security.OperationContext;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditEntity {

    private Boolean isDeleted = false;
    private Long changeUserId;
    private LocalDateTime changeDate;

    @PrePersist
    public void onCreate() {
        this.changeDate = LocalDateTime.now();
        this.changeUserId = OperationContext.accountId();
    }

    @PreUpdate
    public void onUpdate() {
        this.changeDate = LocalDateTime.now();
        this.changeUserId = OperationContext.accountId();
    }
}
