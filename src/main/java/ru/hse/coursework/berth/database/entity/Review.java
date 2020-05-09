package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import ru.hse.coursework.berth.common.EntityWithOwner;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Review implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "berth_id")
    private Berth berth;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private UserInfo userInfo;

    private Integer rating;

    private String text;

    @CreationTimestamp
    private LocalDateTime dateTime;

    @Override
    public Long getOwnerId() {
        return getUserInfo().getAccountId();
    }
}
