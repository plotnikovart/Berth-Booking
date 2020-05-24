package ru.hse.coursework.berth.database.entity.chat;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import ru.hse.coursework.berth.database.entity.AuditEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static ru.hse.coursework.berth.config.DBConfig.NOT_DELETED;

@Getter
@Setter
@Entity
@Where(clause = NOT_DELETED)
public class Chat extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long offset;

    @JoinColumn(name = "last_message_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatMessage lastMessage;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.DETACH, CascadeType.PERSIST})
    private List<ChatAccount> accounts = new ArrayList<>();

    public Chat setAccounts(List<ChatAccount> newAccounts) {
        accounts.clear();
        accounts.addAll(newAccounts);
        return this;
    }
}
