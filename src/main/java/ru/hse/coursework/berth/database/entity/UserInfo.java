package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.CascadeType.DETACH;

@Setter
@Getter
@Entity
@NoArgsConstructor
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

    @OneToMany(mappedBy = "renter", cascade = DETACH)
    @OrderBy("startDate desc")
    private List<Booking> bookings = new ArrayList<>();

    public Long getAccountId() {
        return id;
    }
}
