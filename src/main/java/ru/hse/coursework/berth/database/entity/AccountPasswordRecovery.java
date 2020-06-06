package ru.hse.coursework.berth.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class AccountPasswordRecovery {

    @EmbeddedId
    private PK pk;

    @Column(nullable = false)
    private LocalDateTime expiresAt;


    @Data
    @Embeddable
    public static class PK implements Serializable {

        private String code;
        private String email;
    }
}
