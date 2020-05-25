package ru.hse.coursework.berth.database.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatMessage;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = "select c.chat_id " +
            "from ((select chat_id " +
            "       from chat_account " +
            "       where account_id = ?1) " +
            "      intersect " +
            "      (select chat_id " +
            "       from chat_account " +
            "       where account_id = ?2)) c " +
            "where chat_id in (select chat_id from chat_account group by chat_id having count(*) = 2)", nativeQuery = true)
    List<Long> findAllByParticipants(Long accountId1, Long accountId2);

    @Query("select distinct c from Chat c " +
            "left join fetch c.accounts " +
            "left join fetch c.lastMessage " +
            "where c.lastMessage is not null " +
            "and c.id in (select ca.pk.chatId from ChatAccount ca where ca.account = ?1) " +
            "order by c.lastMessage.sendDateTime desc")
    List<Chat> findAllByAccountWithLoad(Account account);

    @Modifying
    @Transactional
    @Query("update Chat c set " +
            "c.offset = c.offset + 1, " +
            "c.lastMessage = ?2 " +
            "where c = ?1")
    void incrementChatOffset(Chat chat, ChatMessage message);
}
