package ru.hse.coursework.berth.database.entity.chat;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import ru.hse.coursework.berth.database.entity.AuditEntity;
import ru.hse.coursework.berth.database.entity.chat.data.ChatHistoryData;
import ru.hse.coursework.berth.database.entity.chat.data.ChatHistoryDataConverter;

import javax.persistence.*;

import static ru.hse.coursework.berth.config.DBConfig.NOT_DELETED;

@Getter
@Setter
@Entity
@Where(clause = NOT_DELETED)
public class ChatHistory extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "chat_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Chat chat;

    @Column(nullable = false)
    private Long offsetFrom;

    @Column(nullable = false)
    private Long offsetTo;

    @Column(nullable = false)
    @Convert(converter = ChatHistoryDataConverter.class)
    private ChatHistoryData data;
}
