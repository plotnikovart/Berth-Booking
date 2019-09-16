package app.database.entity;

import app.service.file.ImageKind;
import app.web.dto.UserInfoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.MessageFormat;
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
        setAccount(account);
        setDto(userInfoDto);
    }

    public void setDto(UserInfoDto dto) {
        setFirstName(dto.getFirstName());
        setLastName(dto.getLastName());
        setPhCode(dto.getPhCode());
        setPhNumber(dto.getPhNumber());
        setPhotoName(dto.getPhoto());
    }

    public UserInfoDto.WithId getDto() {
        return (UserInfoDto.WithId) new UserInfoDto.WithId()
                .setAccountId(getAccountId())
                .setFirstName(getFirstName())
                .setLastName(getLastName())
                .setPhCode(getPhCode())
                .setPhNumber(getPhNumber())
                .setPhoto(getPhotoName() == null ? null : MessageFormat.format("/api/images/{0}/{1}/{2}", ImageKind.USER.name().toLowerCase(), getAccountId(), getPhotoName()))
                .setEmail(getAccount().getEmail());
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
