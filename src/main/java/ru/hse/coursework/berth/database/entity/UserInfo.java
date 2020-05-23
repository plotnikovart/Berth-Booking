package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
public class UserInfo {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String firstName;

    private String lastName;

    private String phCode;

    private String phNumber;

    private UUID photo;

    private String photoLink;

    @UpdateTimestamp
    private LocalDateTime changeDate = LocalDateTime.now();


    public Long getAccountId() {
        return id;
    }
}
