package ru.hse.coursework.berth.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatAccount;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.database.repository.chat.ChatRepository;
import ru.hse.coursework.berth.service.chat.dto.ChatDto;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

/**
 * Сервис для работы с чатом пользователь - пользователь
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PairChatService {

    private final AccountRepository accountRepository;
    private final ChatService chatService;
    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    public Optional<ChatDto> getChatByParticipant(Long accountId) {
        List<Long> commonChats = chatRepository.findAllByParticipants(accountId, OperationContext.accountId());
        if (commonChats.isEmpty()) {
            return empty();
        }

        if (commonChats.size() > 1) {
            log.warn("Accounts {} and {} has more than one common chats", accountId, OperationContext.accountId());
        }

        return chatService.getChatById(commonChats.get(0));
    }

    @Transactional
    public Long createChat(Long accountId) {
        var chat = new Chat()
                .setLastMessage(null)
                .setOffset(0L);

        var accounts = List.of(
                new ChatAccount()
                        .setAccount(accountRepository.getOne(accountId))
                        .setChat(chat)
                        .setOffset(0L),
                new ChatAccount()
                        .setAccount(accountRepository.getOne(OperationContext.accountId()))
                        .setChat(chat)
                        .setOffset(0L)
        );

        chat.setAccounts(accounts);
        return chatRepository.save(chat).getId();
    }
}
