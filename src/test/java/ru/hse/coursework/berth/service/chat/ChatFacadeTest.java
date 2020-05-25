package ru.hse.coursework.berth.service.chat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.chat.enums.MessageType;
import ru.hse.coursework.berth.database.repository.AbstractAccountTest;
import ru.hse.coursework.berth.database.repository.chat.ChatRepository;
import ru.hse.coursework.berth.service.chat.dto.ChatDto;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.service.chat.dto.OffsetDto;

import java.util.List;

class ChatFacadeTest extends AbstractAccountTest {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        chatRepository.deleteAll();
    }

    @Autowired
    ChatFacade chatFacade;

    @Autowired
    ChatRepository chatRepository;

    @Test
    void chatStart() {
        OperationContext.accountId(user1Account.getId());

        ChatDto chat = chatFacade.startChat(user2Account.getId());
        ChatDto chat2 = chatFacade.startChat(user2Account.getId());
        ChatDto chat3 = chatFacade.startChat(user1Account.getId());
        ChatDto chat4 = chatFacade.getChat(chat.getId());

        Assertions.assertEquals(chat.getId(), chat2.getId());
        Assertions.assertEquals(chat.getId(), chat3.getId());
        Assertions.assertEquals(chat.getId(), chat4.getId());

        Assertions.assertEquals(0, (long) chat.getTotalOffset());
        Assertions.assertEquals(0, (long) chat.getAccountOffset());
        Assertions.assertEquals(2, chat.getParticipants().size());
        Assertions.assertNull(chat.getLastMessage());

        List<ChatDto> allChats = chatFacade.getChats();
        Assertions.assertEquals(0, allChats.size());


        OperationContext.accountId(user2Account.getId());

        chat = chatFacade.getChat(chat.getId());
        Assertions.assertEquals(0, (long) chat.getTotalOffset());
        Assertions.assertEquals(0, (long) chat.getAccountOffset());
        Assertions.assertEquals(2, chat.getParticipants().size());
        Assertions.assertNull(chat.getLastMessage());

        allChats = chatFacade.getChats();
        Assertions.assertEquals(0, allChats.size());
    }

    @Test
    void chatCheckAccess() {
        OperationContext.accountId(user1Account.getId());

        ChatDto chat = chatFacade.startChat(user2Account.getId());


        OperationContext.accountId(moderatorAccount.getId());

        Assertions.assertThrows(AccessException.class, () -> chatFacade.getChat(chat.getId()));
        Assertions.assertThrows(AccessException.class, () -> chatFacade.updateAccountChatOffset(chat.getId(), 12L));
        Assertions.assertThrows(AccessException.class, () -> chatFacade.sendMessage(chat.getId(), new MessageDto().setType(MessageType.TEXT).setText("text")));
        Assertions.assertThrows(AccessException.class, () -> chatFacade.getMessages(chat.getId(), new OffsetDto().setOffsetStart(0L).setOffsetEnd(0L)));
    }
}