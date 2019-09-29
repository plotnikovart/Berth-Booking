package app.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.DETACH;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class UserInfo {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String firstName;

    private String lastName;

    private String phCode;

    private String phNumber;

    private String photoName;

    @OneToMany(mappedBy = "userInfo", cascade = DETACH, orphanRemoval = true)
    private List<Ship> ships = new ArrayList<>();

    @OneToMany(mappedBy = "userInfo", cascade = DETACH, orphanRemoval = true)
    private List<Berth> berths = new ArrayList<>();

    @OneToMany(mappedBy = "renter", cascade = DETACH)
    @OrderBy("startDate desc")
    private List<Booking> bookings = new ArrayList<>();

    public Long getAccountId() {
        return id;
    }
}
