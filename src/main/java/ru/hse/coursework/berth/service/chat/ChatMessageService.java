package ru.hse.coursework.berth.service.chat;

import de.jkeylockmanager.manager.KeyLockManager;
import de.jkeylockmanager.manager.implementation.lockstripe.StripedKeyLockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatMessage;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.database.repository.chat.ChatMessageRepository;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.service.chat.dto.OffsetDto;
import ru.hse.coursework.berth.service.converters.impl.UserInfoConverter;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final EntityManager em;
    private final KeyLockManager lockManager = new StripedKeyLockManager(1, TimeUnit.MINUTES, 1000);
    private final ChatMessageRepository chatMessageRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserInfoConverter userInfoConverter;

    MessageDto.Resp sendMessage(Long chatId, MessageDto messageDto) {
        Chat chat = em.getReference(Chat.class, chatId);
        Account account = em.getReference(Account.class, OperationContext.accountId());

        return lockManager.executeLocked(chatId, () -> {
            Long offset = ofNullable(chatMessageRepository.findMaxChatOffset(chat)).orElse(0L);

            var message = new ChatMessage()
                    .setOffset(offset + 1)
                    .setMessageType(messageDto.getType())
                    .setMessageText(messageDto.getText())
                    .setChat(chat)
                    .setSender(account);

            chatMessageRepository.save(message);
            return toDto(message);
        });
    }


    List<MessageDto.Resp> getMessages(Long chatId, OffsetDto offsetDto) {
        Chat chat = em.getReference(Chat.class, chatId);
        List<MessageDto.Resp> messages = chatMessageRepository.findAllByChat(chat, offsetDto.getOffsetStart(), offsetDto.getOffsetStart()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (messages.size() < offsetDto.getTargetNum()) {
            // todo load from history
        }

        return messages;
    }

    private MessageDto.Resp toDto(ChatMessage message) {
        return (MessageDto.Resp) new MessageDto.Resp()
                .setId(message.getId())
                .setOffset(message.getOffset())
                .setSendDateTime(message.getSendDateTime())
                .setParticipantId(message.getSender().getId())
                .setText(message.getMessageText())
                .setType(message.getMessageType());
    }
}
