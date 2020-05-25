package ru.hse.coursework.berth.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.CacheConfig;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatAccount;
import ru.hse.coursework.berth.database.repository.chat.ChatRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatPermissionService {

    private final ChatRepository chatRepository;

    @Transactional(readOnly = true)
    @Cacheable(CacheConfig.HAS_CHAT_ACCESS)
    public boolean hasChatAccess(Long chatId, Long accountId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);

        Optional<ChatAccount> acc = chat.getAccounts().stream()
                .filter(it -> it.getPk().getAccountId().equals(accountId))
                .findAny();

        return acc.isPresent();
    }
}
