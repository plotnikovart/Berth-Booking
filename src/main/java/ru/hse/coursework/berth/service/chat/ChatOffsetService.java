package ru.hse.coursework.berth.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatMessage;
import ru.hse.coursework.berth.database.repository.chat.ChatAccountRepository;
import ru.hse.coursework.berth.database.repository.chat.ChatRepository;
import ru.hse.coursework.berth.service.event.chat.MessageSendEvent;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class ChatOffsetService {

    private final EntityManager em;
    private final ChatRepository chatRepository;
    private final ChatAccountRepository chatAccountRepository;

    @EventListener
    public void newMessageSend(MessageSendEvent event) {
        Chat chat = em.getReference(Chat.class, event.getChatId());
        ChatMessage message = em.getReference(ChatMessage.class, event.getMessageDto().getId());
        Account account = em.getReference(Account.class, event.getMessageDto().getParticipantId());

        chatRepository.incrementChatOffset(chat, message);
        chatAccountRepository.updateChatAccountOffset(chat, account, event.getMessageDto().getOffset());
    }

    void updateAccountOffset(Long chatId, Long offset) {
        Chat chat = em.getReference(Chat.class, chatId);
        Account account = em.getReference(Account.class, OperationContext.accountId());

        // обновит только в случае, если offset свежий
        chatAccountRepository.updateChatAccountOffset(chat, account, offset);
    }
}
