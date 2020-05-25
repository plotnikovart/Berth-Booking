package ru.hse.coursework.berth.service.chat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hse.coursework.berth.config.exception.impl.AccessException;
import ru.hse.coursework.berth.config.security.OperationContext;
import ru.hse.coursework.berth.database.entity.chat.enums.MessageType;
import ru.hse.coursework.berth.database.repository.AbstractAccountTest;
import ru.hse.coursework.berth.database.repository.chat.ChatAccountRepository;
import ru.hse.coursework.berth.database.repository.chat.ChatRepository;
import ru.hse.coursework.berth.service.chat.dto.ChatDto;
import ru.hse.coursework.berth.service.chat.dto.MessageDto;
import ru.hse.coursework.berth.service.chat.dto.OffsetDto;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Autowired
    ChatAccountRepository chatAccountRepository;

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

    @Test
    void chatMessaging() {
        OperationContext.accountId(user1Account.getId());
        ChatDto chat = chatFacade.startChat(user2Account.getId());

        // 1 сообщение - пользователь 1
        var message1 = new MessageDto()
                .setType(MessageType.TEXT)
                .setText("Это первое сообщение!");

        Long message1Id = chatFacade.sendMessage(chat.getId(), message1);

        List<ChatDto> allChats = chatFacade.getChats();
        Assertions.assertEquals(1, allChats.size());

        chat = allChats.get(0);
        Assertions.assertEquals(1, (long) chat.getAccountOffset());
        Assertions.assertEquals(1, (long) chat.getTotalOffset());
        Assertions.assertEquals(message1Id, chat.getLastMessage().getId());


        // 2 сообщение - пользователь 1
        var message2 = new MessageDto()
                .setType(MessageType.TEXT)
                .setText("Это второе сообщение!");

        Long message2Id = chatFacade.sendMessage(chat.getId(), message2);

        allChats = chatFacade.getChats();
        Assertions.assertEquals(1, allChats.size());

        chat = allChats.get(0);
        Assertions.assertEquals(2, (long) chat.getAccountOffset());
        Assertions.assertEquals(2, (long) chat.getTotalOffset());
        Assertions.assertEquals(message2Id, chat.getLastMessage().getId());


        // 3 сообщение - пользователь 2
        OperationContext.accountId(user2Account.getId());

        allChats = chatFacade.getChats();
        Assertions.assertEquals(1, allChats.size());

        chat = allChats.get(0);
        Assertions.assertEquals(0, (long) chat.getAccountOffset());
        Assertions.assertEquals(2, (long) chat.getTotalOffset());
        Assertions.assertEquals(message2Id, chat.getLastMessage().getId());

        var message3 = new MessageDto()
                .setType(MessageType.TEXT)
                .setText("Это третье сообщение!");

        Long message3Id = chatFacade.sendMessage(chat.getId(), message3);

        allChats = chatFacade.getChats();
        Assertions.assertEquals(1, allChats.size());

        chat = allChats.get(0);
        Assertions.assertEquals(3, (long) chat.getAccountOffset());
        Assertions.assertEquals(3, (long) chat.getTotalOffset());
        Assertions.assertEquals(message3Id, chat.getLastMessage().getId());


        // 4 сообщение - пользователь 1
        OperationContext.accountId(user1Account.getId());

        var message4 = new MessageDto()
                .setType(MessageType.TEXT)
                .setText("Это четвертое сообщение!");

        Long message4Id = chatFacade.sendMessage(chat.getId(), message4);

        allChats = chatFacade.getChats();
        Assertions.assertEquals(1, allChats.size());

        chat = allChats.get(0);
        Assertions.assertEquals(4, (long) chat.getAccountOffset());
        Assertions.assertEquals(4, (long) chat.getTotalOffset());
        Assertions.assertEquals(message4Id, chat.getLastMessage().getId());


        // получение 3-х последних сообщений чата
        List<MessageDto.Resp> messages = chatFacade.getMessages(chat.getId(), new OffsetDto().setOffsetStart(2L).setOffsetEnd(4L));

        Assertions.assertEquals(3, messages.size());
        // check order
        Assertions.assertEquals(message2Id, messages.get(0).getId());
        Assertions.assertEquals(message3Id, messages.get(1).getId());
        Assertions.assertEquals(message4Id, messages.get(2).getId());
        // check offset
        Assertions.assertEquals(2, (long) messages.get(0).getOffset());
        Assertions.assertEquals(3, (long) messages.get(1).getOffset());
        Assertions.assertEquals(4, (long) messages.get(2).getOffset());
        // check participants
        Assertions.assertEquals(user1Account.getId(), messages.get(0).getParticipantId());
        Assertions.assertEquals(user2Account.getId(), messages.get(1).getParticipantId());
        Assertions.assertEquals(user1Account.getId(), messages.get(2).getParticipantId());


        // получение в максимальных границах сообщений чата
        messages = chatFacade.getMessages(chat.getId(), new OffsetDto().setOffsetStart(0L).setOffsetEnd(Long.MAX_VALUE));
        Assertions.assertEquals(4, messages.size());
    }

    @Test
    void offsetUpdateTest() {
        OperationContext.accountId(user1Account.getId());
        ChatDto chat = chatFacade.startChat(user2Account.getId());

        var message = new MessageDto()
                .setType(MessageType.TEXT)
                .setText("Это первое сообщение!");

        chatFacade.sendMessage(chat.getId(), message);
        chatFacade.sendMessage(chat.getId(), message);
        chatFacade.sendMessage(chat.getId(), message);

        OperationContext.accountId(user2Account.getId());
        chat = chatFacade.getChat(chat.getId());

        Assertions.assertEquals(3, (long) chat.getTotalOffset());
        Assertions.assertEquals(0, (long) chat.getAccountOffset());

        // прочитал сообщения
        chatFacade.updateAccountChatOffset(chat.getId(), 3L);

        chat = chatFacade.getChat(chat.getId());
        Assertions.assertEquals(3, (long) chat.getTotalOffset());
        Assertions.assertEquals(3, (long) chat.getAccountOffset());

        // прочитал слишком много сообщений (это ошибка фронта), корректно ее отработам
        chatFacade.updateAccountChatOffset(chat.getId(), 10L);

        chat = chatFacade.getChat(chat.getId());
        Assertions.assertEquals(3, (long) chat.getTotalOffset());
        Assertions.assertEquals(3, (long) chat.getAccountOffset());

        // прочитал слишком мало сообщений (это ошибка фронта), корректно ее отработам
        chatFacade.updateAccountChatOffset(chat.getId(), 1L);

        chat = chatFacade.getChat(chat.getId());
        Assertions.assertEquals(3, (long) chat.getTotalOffset());
        Assertions.assertEquals(3, (long) chat.getAccountOffset());
    }

    @Test
    @Disabled
    void offsetStressTest() throws Exception {
        OperationContext.accountId(user1Account.getId());

        ChatDto chat = chatFacade.startChat(user2Account.getId());

        var user1Message = new MessageDto().setType(MessageType.TEXT).setText("1");
        var user2Message = new MessageDto().setType(MessageType.TEXT).setText("2");

        ExecutorService executor1 = Executors.newFixedThreadPool(5);
        ExecutorService executor2 = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 1000; i++) {
            executor1.submit(() -> {
                OperationContext.accountId(user1Account.getId());
                chatFacade.sendMessage(chat.getId(), user1Message);
            });
            executor2.submit(() -> {
                OperationContext.accountId(user2Account.getId());
                chatFacade.sendMessage(chat.getId(), user2Message);
            });
        }

        executor1.shutdown();
        executor1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        executor2.shutdown();
        executor2.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        ChatDto chat2 = chatFacade.getChat(chat.getId());
        Assertions.assertEquals(2000, (long) chat2.getTotalOffset());
        Assertions.assertEquals(2000, (long) chat2.getLastMessage().getOffset());

        List<MessageDto.Resp> messages = chatFacade.getMessages(chat.getId(), new OffsetDto().setOffsetStart(1L).setOffsetEnd(2000L));
        Set<Long> offsets = messages.stream().map(MessageDto.Resp::getOffset).collect(Collectors.toSet());
        Assertions.assertEquals(2000, offsets.size());
    }
}