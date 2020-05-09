package ru.hse.coursework.berth.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class AccountConfirmation {

    @Id
    private UUID uuid;

    @NotNull
    private String email;
    @NotNull
    private byte[] passwordHash;
    @NotNull
    private byte[] salt;

    @NotNull
    private LocalDateTime expiredAt;
    @NotNull
    private Boolean confirmed = false;
}
