package ru.hse.coursework.berth.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.exception.impl.NotFoundException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.service.chat.dto.ChatDto;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.service.chat.dto.OffsetDto;
import ru.hse.coursework.berth.service.event.EventPublisher;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final EventPublisher eventPublisher;
    private final ChatPermissionService chatPermissionService;
    private final ChatService chatService;
    private final PairChatService pairChatService;
    private final ChatOffsetService chatOffsetService;
    private final ChatMessageService chatMessageService;

    public ChatDto startChat(Long accountId) {
        Optional<ChatDto> chatDtoOpt = pairChatService.getChatByParticipant(accountId);
        if (chatDtoOpt.isPresent()) {
            return chatDtoOpt.get();
        }

        long chatId = pairChatService.createChat(accountId);
        return chatService.getChatById(chatId).orElseThrow();
    }

    public ChatDto getChat(Long chatId) {
        checkChatAssess(chatId);
        return chatService.getChatById(chatId).orElseThrow(NotFoundException::new);
    }

    public List<ChatDto> getChats() {
        return chatService.getChats();
    }

    public void updateAccountChatOffset(Long chatId, Long offset) {
        checkChatAssess(chatId);
        chatOffsetService.updateAccountOffset(chatId, offset);
    }

    public Long sendMessage(Long chatId, MessageDto messageDto) {
        checkChatAssess(chatId);
        MessageDto.Resp dto = chatMessageService.sendMessage(chatId, messageDto);

        eventPublisher.sendMessage(chatId, dto);
        return dto.getId();
    }

    public List<MessageDto.Resp> getMessages(Long chatId, OffsetDto offsetDto) {
        checkChatAssess(chatId);
        return chatMessageService.getMessages(chatId, offsetDto);
    }


    private void checkChatAssess(Long chatId){
        boolean hasAccess = chatPermissionService.hasChatAccess(chatId, OperationContext.accountId());
        if (!hasAccess){
            throw new AccessException();
        }
    }
}
