package app.database.entity;

import app.common.EntityWithOwner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Review implements EntityWithOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "berth_id")
    private Berth berth;

    @ManyToOne(optional = false)
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
