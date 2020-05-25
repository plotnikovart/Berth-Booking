package ru.hse.coursework.berth.database.entity.chat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.hse.coursework.berth.database.entity.Account;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class ChatAccount {

    @EmbeddedId
    private PK pk = new PK();

    @MapsId("chatId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Chat chat;

    @MapsId("accountId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(nullable = false, name = "offset_")
    private Long offset;

    @Data
    @Embeddable
    public static class PK implements Serializable {

        private Long chatId;
        private Long accountId;
    }
}
