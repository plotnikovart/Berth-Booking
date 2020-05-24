package ru.hse.coursework.berth.database.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatMessage;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select max(ch.offset) from ChatMessage ch where ch.chat = ?1")
    Long findMaxChatOffset(Chat chat);


    @Query("select ch from ChatMessage ch " +
            "where ch.chat = ?1 and ch.offset between ?2 and ?3 " +
            "order by ch.offset")
    List<ChatMessage> findAllByChat(Chat chat, long offsetStart, long offsetEnd);
}
