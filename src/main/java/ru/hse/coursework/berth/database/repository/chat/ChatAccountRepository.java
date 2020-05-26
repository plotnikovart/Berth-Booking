package ru.hse.coursework.berth.database.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatAccount;

import java.util.List;

@Repository
public interface ChatAccountRepository extends JpaRepository<ChatAccount, ChatAccount.PK> {


    @Modifying
    @Transactional
    @Query("update ChatAccount ca set ca.offset = ?3 " +
            "where ca.chat = ?1 and ca.account = ?2 and ca.offset < ?3")
    void updateChatAccountOffset(Chat chat, Account account, Long newOffset);

    List<ChatAccount> findAllByChat(Chat chat);
}
