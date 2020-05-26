package ru.hse.coursework.berth.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.hse.coursework.berth.database.entity.chat.Chat;
import ru.hse.coursework.berth.database.entity.chat.ChatAccount;
import ru.hse.coursework.berth.database.repository.chat.ChatAccountRepository;
import ru.hse.coursework.berth.service.event.chat.MessageSendEvent;
import ru.hse.coursework.berth.websocket.SocketMessageSender;
import ru.hse.coursework.berth.websocket.event.OutgoingEventEnum;
import ru.hse.coursework.berth.websocket.event.outgoing.ChatMessageOutgoingDto;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageNotifier {

    private final EntityManager em;
    private final ChatAccountRepository chatAccountRepository;
    private final SocketMessageSender socketMessageSender;

    @EventListener
    public void newMessageSend(MessageSendEvent event) {
        Chat chat = em.getReference(Chat.class, event.getChatId());
        List<ChatAccount> chatParticipants = chatAccountRepository.findAllByChat(chat);

        var data = new ChatMessageOutgoingDto.D()
                .setChatId(event.getChatId())
                .setMessage(event.getMessageDto());

        var message = new ChatMessageOutgoingDto()
                .setData(data)
                .setEvent(OutgoingEventEnum.CHAT_MESSAGE);

        chatParticipants.stream()
                .filter(it -> !it.getPk().getAccountId().equals(event.getMessageDto().getParticipantId()))
                .forEach(it -> socketMessageSender.sendMessage(it.getPk().getAccountId(), message));
    }
}
