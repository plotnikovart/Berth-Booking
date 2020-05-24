package ru.hse.coursework.berth.service.chat;

import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.Account;
import ru.hse.coursework.berth.database.entity.UserInfo;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatAccount;
import ru.hse.coursework.berth.database.repository.AccountRepository;
import ru.hse.coursework.berth.database.repository.UserInfoRepository;
import ru.hse.coursework.berth.database.repository.chat.ChatRepository;
import ru.hse.coursework.berth.service.account.dto.UserInfoDto;
import ru.hse.coursework.berth.service.chat.dto.ChatDto;
import ru.hse.coursework.berth.service.chat.dto.ParticipantDto;
import ru.hse.coursework.berth.service.converters.impl.UserInfoConverter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Сервис для работы с чатом
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserInfoConverter userInfoConverter;
    private final UserInfoRepository userInfoRepository;
    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;


    @Transactional(readOnly = true)
    public Optional<ChatDto> getChatById(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(NotFoundException::new);
        ChatDto chatDto = toDto(chat);
        fillParticipantsFullInfo(List.of(chatDto));

        return Optional.ofNullable(chatDto);
    }

    public List<ChatDto> getChats() {
        Account account = accountRepository.getOne(OperationContext.accountId());
        List<Chat> chats = chatRepository.findAllByAccount(account);

        List<ChatDto> dtos = chats.stream().map(this::toDto).collect(Collectors.toList());
        return fillParticipantsFullInfo(dtos);
    }

    private ChatDto toDto(Chat chat) {
        Long accountOffset = chat.getAccounts().stream()
                .filter(it -> it.getPk().getAccountId().equals(OperationContext.accountId()))
                .map(ChatAccount::getOffset)
                .findFirst().orElseThrow();

        List<ParticipantDto> participants = chat.getAccounts().stream()
                .map(it -> new ParticipantDto()
                        .setAccountId(it.getPk().getAccountId()))
                .collect(Collectors.toList());

        return new ChatDto()
                .setId(chat.getId())
                .setTotalOffset(chat.getOffset())
                .setLastMessage(null)   // todo
                .setAccountOffset(accountOffset)
                .setParticipants(participants);
    }

    private List<ChatDto> fillParticipantsFullInfo(List<ChatDto> dtos) {
        if (dtos.isEmpty()) {
            return dtos;
        }

        List<Long> ids = StreamEx.of(dtos).flatMap(it -> it.getParticipants().stream().map(ParticipantDto::getAccountId)).toList();
        Map<Long, UserInfoDto.Resp> userInfoMap = StreamEx.of(userInfoRepository.findAllById(ids))
                .toMap(UserInfo::getAccountId, userInfoConverter::toDto);

        dtos.stream().flatMap(it -> it.getParticipants().stream())
                .forEach(participant -> ofNullable(userInfoMap.get(participant.getAccountId()))
                        .ifPresent(userInfo -> participant
                                .setFirstName(userInfo.getFirstName())
                                .setLastName(userInfo.getLastName())
                                .setPhotoLink(userInfo.getPhoto().getFileLink())
                        )
                );

        return dtos;
    }
}
