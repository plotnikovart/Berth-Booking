package ru.hse.coursework.berth.database.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.coursework.berth.database.entity.chat.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
