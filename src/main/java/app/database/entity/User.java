package app.database.entity;

import app.web.dto.UserDto;
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
@Table(name = "user_")
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String firstName;

    private String lastName;

    private String phCode;

    private String phNumber;

    private String photoName;

    @OneToMany(mappedBy = "user", cascade = {PERSIST, DETACH, MERGE}, orphanRemoval = true)
    private List<Ship> ships = new ArrayList<>();

    public User(Account account, UserDto userDto) {
        this.account = account;
        setDto(userDto);
    }

    public void setDto(UserDto userDto) {
        firstName = userDto.getFirstName();
        lastName = userDto.getLastName();
        phCode = userDto.getPhCode();
        phNumber = userDto.getPhNumber();
        photoName = userDto.getPhotoName();
    }

    private Long getId() {
        return id;
    }

    public Long getAccountId() {
        return id;
    }
}
