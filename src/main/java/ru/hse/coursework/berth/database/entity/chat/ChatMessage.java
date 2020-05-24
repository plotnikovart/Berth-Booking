package ru.hse.coursework.berth.database.entity.chat;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.AuditEntity;
import ru.hse.coursework.berth.database.entity.chat.enums.MessageType;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.hse.coursework.berth.config.DBConfig.NOT_DELETED;

@Getter
@Setter
@Entity
@Where(clause = NOT_DELETED)
public class ChatMessage extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "chat_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @Column(nullable = false)
    private Long offset;

    @JoinColumn(name = "sender_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account sender;

    @Setter(AccessLevel.NONE)
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime sendDateTime;

    @Column(nullable = false)
    @Convert(converter = MessageType.Converter.class)
    private MessageType messageType;

    private String messageText;
}
