package app.database.entity;

import app.database.entity.enums.AccountKind;
import app.database.entity.enums.AccountRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = AccountKind.Converter.class)
    private AccountKind kind;

    private String email;
    private byte[] passwordHash;
    private byte[] salt;

    private String googleMail;

    @NotNull
    private LocalDateTime joinDateTime = LocalDateTime.now();

    @ElementCollection(targetClass = AccountRole.class)
    @CollectionTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role")
    @Convert(converter = AccountRole.Converter.class)
    private Set<AccountRole> roles = new HashSet<>();


//    public Account(EmailCredential emailCredential) {
//        email = emailCredential.getEmail();
//        salt = new String(new SecureRandom().generateSeed(16), StandardCharsets.US_ASCII);
//        passwordHash = createPasswordHash(emailCredential.getPassword(), salt);
//        roles.add(Role.USER);
//    }
//
//    private String createPasswordHash(String password, String salt) {
//        byte[] byteArr = BCrypt.withDefaults().hash(6, salt.getBytes(StandardCharsets.US_ASCII), password.getBytes());
//        return new String(byteArr, StandardCharsets.US_ASCII);
//    }
//
//    public boolean checkPassword(String password) {
//        String passwordHash = createPasswordHash(password, salt);
//        return this.passwordHash.equals(passwordHash);
//    }
}
