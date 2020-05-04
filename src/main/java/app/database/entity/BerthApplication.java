package app.database.entity;

import app.database.entity.enums.BerthApplicationStatus;
import com.google.api.client.repackaged.com.google.common.base.Splitter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Column(nullable = false)
    private String title;
    private String description;
    private String attachments;

    @Convert(converter = BerthApplicationStatus.Converter.class)
    @Column(nullable = false)
    private BerthApplicationStatus status;

    private String decision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private Account moderator;


    public BerthApplication setAttachments(@Nullable List<UUID> attachments) {
        this.attachments = attachments == null ? null : attachments.stream().map(UUID::toString).collect(Collectors.joining());
        return this;
    }

    public List<UUID> getAttachements() {
        if (StringUtils.isBlank(attachments)) {
            return List.of();
        }
        Iterable<String> split = Splitter.fixedLength(36).split(this.attachments);
        return StreamSupport.stream(split.spliterator(), false)
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }

    private void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
