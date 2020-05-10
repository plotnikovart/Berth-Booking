package ru.hse.coursework.berth.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.hse.coursework.berth.common.EntityWithOwner;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Review extends AuditEntity implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Account reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id", nullable = false)
    private Berth berth;

    @Column(nullable = false)
    private Integer rating;

    private String text;

    @CreationTimestamp
    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime createdAt;

    @Override
    public Long getOwnerId() {
        return reviewer.getId();
    }
}
