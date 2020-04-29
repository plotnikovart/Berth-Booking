package app.database.entity;

import app.database.entity.enums.BerthApplicationStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

import static app.config.DBConfig.NOT_DELETED;

@Getter
@Setter
@Entity
@Where(clause = NOT_DELETED)
public class BerthApplication extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Account applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id", nullable = false)
    private Berth berth;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime creationDate;

    private String description;
    private String attachments;

    @Convert(converter = BerthApplicationStatus.Converter.class)
    @Column(nullable = false)
    private BerthApplicationStatus status;

    private String decision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", nullable = false)
    private Account moderator;
}
