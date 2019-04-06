package app.database.model;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.CascadeType.PERSIST;

@Getter
@Setter
@Entity
@Table(name = "user_")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Length(max = 30, message = "Ограничение на длину имени 30 символов")
    private String name;
    @Column
    @Length(max = 40, message = "Ограничение на длину фамилии 40 символов")
    private String surname;
    @Column
    @NotNull(message = "Не задан номер телефона")
    @Length(max = 20, message = "Ограничение на длину номера телефона 20 символов")
    private String phoneNum;
    @Column
    @NotNull(message = "Не задана электронная почта")
    @Length(max = 255, message = "Ограничение на длину электронной почты 255 символов")
    private String email;
    @Column
    @NotNull(message = "Не задан пароль")
    private String passwordHash;
    @Column
    @NotNull
    private String salt;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] photo;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_to_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {PERSIST, DETACH}, orphanRemoval = true)
    private List<Ship> ships = new ArrayList<>();

    public User() {
        salt = new String(new SecureRandom().generateSeed(16), StandardCharsets.US_ASCII);
    }

    public void setPassword(String password) {
        this.passwordHash = createPasswordHash(password, salt);
    }

    private void setPasswordHash(String passwordHash) {}

    private void setSalt(String salt) {}

    public static String createPasswordHash(String password, String salt) {
        byte[] byteArr = BCrypt.withDefaults().hash(6, salt.getBytes(StandardCharsets.US_ASCII), password.getBytes());
        return new String(byteArr, StandardCharsets.US_ASCII);
    }
}
