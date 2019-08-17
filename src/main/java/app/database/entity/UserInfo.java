package app.database.entity;

import app.web.dto.UserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

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

    @OneToMany(mappedBy = "userInfo", cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
    private List<Ship> ships = new ArrayList<>();

    @OneToMany(mappedBy = "userInfo", cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
    private List<Berth> berths = new ArrayList<>();

    @OneToMany(mappedBy = "renter", cascade = DETACH)
    @OrderBy("startDate desc")
    private List<Booking> bookings = new ArrayList<>();

    public UserInfo(Account account, UserInfoDto userInfoDto) {
        this.account = account;
        setDto(userInfoDto);
    }

    public void setDto(UserInfoDto userInfoDto) {
        firstName = userInfoDto.getFirstName();
        lastName = userInfoDto.getLastName();
        phCode = userInfoDto.getPhCode();
        phNumber = userInfoDto.getPhNumber();
        photoName = userInfoDto.getPhoto();
    }

    public UserInfoDto.WithId getDto() {
        return (UserInfoDto.WithId) new UserInfoDto.WithId()
                .setAccountId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPhCode(phCode)
                .setPhNumber(phNumber)
                .setPhoto(photoName);
    }

    private Long getId() {
        return id;
    }

    public void setShips(List<Ship> ships) {
        this.ships.clear();
        this.ships.addAll(ships);
    }

    public void setBerths(List<Berth> berths) {
        this.berths.clear();
        this.berths.addAll(berths);
    }

    public Long getAccountId() {
        return id;
    }
}
