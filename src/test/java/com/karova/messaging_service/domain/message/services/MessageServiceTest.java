package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;
import com.karova.messaging_service.domain.user.models.MsgUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;


    private static final UUID MOCK_USER_ID = UUID.randomUUID();
    private static final String MOCK_MESSAGE_CONTENT = "mock message content";
    private static final UUID MOCK_MESSAGE_ID_1 = UUID.randomUUID();
    private static final UUID MOCK_MESSAGE_ID_2 = UUID.randomUUID();
    private static final LocalDateTime MOCK_TIMESTAMP_EARLIEST =
            LocalDateTime.of(2024, 02, 10, 14, 22);
    private static final LocalDateTime MOCK_TIMESTAMP_MIDDLE =
            LocalDateTime.of(2024, 10, 25, 15, 32);
    private static final LocalDateTime MOCK_TIMESTAMP_LATEST =
            LocalDateTime.of(2024, 10, 27, 18, 57);
    private static final Message MOCK_MESSAGE_1 = new Message(
            MOCK_MESSAGE_ID_1,
            new MsgUser(),
            new MsgUser(),
            MOCK_MESSAGE_CONTENT,
            MOCK_TIMESTAMP_EARLIEST,
            false
    );
    private static final Message MOCK_MESSAGE_2 = new Message(
            MOCK_MESSAGE_ID_2,
            new MsgUser(),
            new MsgUser(),
            MOCK_MESSAGE_CONTENT,
            MOCK_TIMESTAMP_MIDDLE,
            false
    );
    private static final Message MOCK_MESSAGE_3 = new Message(
            MOCK_MESSAGE_ID_2,
            new MsgUser(),
            new MsgUser(),
            MOCK_MESSAGE_CONTENT,
            MOCK_TIMESTAMP_LATEST,
            true
    );

    @Test
    void shouldReturn_ListSize1_ListOfTypeMessage_And_UpdateReadToTrue_And() {
        List<Message> messages = List.of(MOCK_MESSAGE_1);
        when(messageRepository.findAllByReadFalseAndReceiverId(MOCK_USER_ID))
                .thenReturn(Optional.of(messages));
        int expectedSize = 1;
        List<Message> actualResult = messageService.getAllNewMessagesByReceiverId(MOCK_USER_ID);
        assertEquals(expectedSize, actualResult.size());
        assertInstanceOf(Message.class, actualResult.getFirst());
        assertTrue(actualResult.getFirst().isRead());
    }

    @Test
    void shouldReturn_EmptyList_WhenNewMessagesNotFound() {
        when(messageRepository.findAllByReadFalseAndReceiverId(MOCK_USER_ID))
                .thenReturn(Optional.of(new ArrayList<>()));
        int expectedSize = 0;
        List<Message> actualResult = messageService.getAllNewMessagesByReceiverId(MOCK_USER_ID);
        assertEquals(expectedSize, actualResult.size());
    }

    @Test
    void shouldReturn_ListSize2_ListOfTypeMessage_And_UpdateReadToTrue_And() {
        List<Message> messages = List.of(MOCK_MESSAGE_1, MOCK_MESSAGE_2);
        when(messageRepository.findAllByReceiverIdOrderByDateSentDesc(MOCK_USER_ID))
                .thenReturn(Optional.of(messages));
        int expectedSize = 2;
        List<Message> actualResult = messageService.getAllMessagesByReceiverId(MOCK_USER_ID);
        assertEquals(expectedSize, actualResult.size());
        assertInstanceOf(Message.class, actualResult.getFirst());
        assertTrue(actualResult.getFirst().isRead());
    }

    @Test
    void shouldReturn_OrderedMessages_LatestFirst_EarliestLast() {

        List<Message> messages = List.of(MOCK_MESSAGE_3, MOCK_MESSAGE_2, MOCK_MESSAGE_1);
        when(messageRepository.findAllByReceiverIdOrderByDateSentDesc(MOCK_USER_ID))
                .thenReturn(Optional.of(messages));
        List<Message> actualResult = messageService.getAllMessagesByReceiverId(MOCK_USER_ID);
        assertTrue(actualResult.getFirst().getDateSent().isAfter(actualResult.get(1).getDateSent()));
        assertTrue(actualResult.getFirst().getDateSent().isAfter(actualResult.get(2).getDateSent()));
        assertTrue(actualResult.get(1).getDateSent().isAfter(actualResult.get(2).getDateSent()));
    }

    @Test
    void shouldReturn_ListSize3_ListOfTypeMessage_And_UpdateReadToTrue_And() {
        List<Message> messages = List.of(MOCK_MESSAGE_3, MOCK_MESSAGE_2, MOCK_MESSAGE_1);
        when(messageRepository.findAllByReceiverIdOrderByDateSentDesc(MOCK_USER_ID))
                .thenReturn(Optional.of(messages));
        int expectedSize = 3;
        List<Message> actualResult = messageService.getAllMessagesByReceiverId(MOCK_USER_ID);
        assertEquals(expectedSize, actualResult.size());
        assertInstanceOf(Message.class, actualResult.getFirst());
        assertTrue(actualResult.getFirst().isRead());
    }
}