package app.database.entity;

import app.database.entity.enums.Role;
import app.web.dto.AccountDto;
import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String email;

    private String passwordHash;

    private String salt;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "account_to_role", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    public Account(AccountDto accountDto) {
        email = accountDto.getEmail();
        salt = new String(new SecureRandom().generateSeed(16), StandardCharsets.US_ASCII);
        passwordHash = createPasswordHash(accountDto.getPassword(), salt);
        roles.add(Role.USER);
    }

    private String createPasswordHash(String password, String salt) {
        byte[] byteArr = BCrypt.withDefaults().hash(6, salt.getBytes(StandardCharsets.US_ASCII), password.getBytes());
        return new String(byteArr, StandardCharsets.US_ASCII);
    }

    public boolean checkPassword(String password) {
        String passwordHash = createPasswordHash(password, salt);
        return this.passwordHash.equals(passwordHash);
    }
}
