package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;
import ru.hse.coursework.berth.database.entity.enums.AccountKind;
import ru.hse.coursework.berth.database.entity.enums.AccountRole;

import javax.persistence.*;
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

    @Column(nullable = false)
    @Convert(converter = AccountKind.Converter.class)
    private AccountKind kind;

    private String email;
    private byte[] passwordHash;
    private byte[] salt;

    private String googleMail;

    private Long facebookId;
    private String facebookMail;

    @Column(nullable = false)
    private LocalDateTime joinDateTime = LocalDateTime.now();

    @ElementCollection(targetClass = AccountRole.class)
    @CollectionTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role")
    @Convert(converter = AccountRole.Converter.class)
    private Set<AccountRole> roles = new HashSet<>();
}
