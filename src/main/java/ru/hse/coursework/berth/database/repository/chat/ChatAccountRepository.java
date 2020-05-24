package ru.hse.coursework.berth.database.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.chat.ChatAccount;

@Repository
public interface ChatAccountRepository extends JpaRepository<ChatAccount, ChatAccount.PK> {
}
