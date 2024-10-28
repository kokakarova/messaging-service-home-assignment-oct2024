package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;
import com.karova.messaging_service.domain.msguser.models.MsgUser;
import com.karova.messaging_service.domain.msguser.services.MsgUserService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private MsgUserService userService;

    private static final UUID MOCK_USER_ID = UUID.randomUUID();
    private static final String MOCK_MESSAGE_CONTENT = "mock message content";
    private static final UUID MOCK_MESSAGE_ID_1 = UUID.randomUUID();
    private static final UUID MOCK_MESSAGE_ID_2 = UUID.randomUUID();
    private static final LocalDateTime MOCK_TIMESTAMP_EARLIEST =
            LocalDateTime.of(2024, 2, 10, 14, 22);
    private static final LocalDateTime MOCK_TIMESTAMP_MIDDLE =
            LocalDateTime.of(2024, 10, 25, 15, 32);
    private static final LocalDateTime MOCK_TIMESTAMP_LATEST =
            LocalDateTime.of(2024, 10, 27, 18, 57);
    private static final String MOCK_SENDER_ID = "2f3197d6-f0d9-480a-9781-1588012e3e73";
    private static final String MOCK_RECEIVER_ID = "7f3191d6-f0d9-480a-9781-1588012e3e55";
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
    private static final SaveMessageReqDto MOCK_MESSAGE_REQUEST =
            new SaveMessageReqDto(MOCK_SENDER_ID, MOCK_RECEIVER_ID, MOCK_MESSAGE_CONTENT);
    Pageable pageRequest = PageRequest.of(0, 10);

    @Test
    void shouldReturn_TypeMessage_AndCorrectMessageContent() {
        when(messageRepository.save(any(Message.class))).thenReturn(MOCK_MESSAGE_1);
        when(userService.getUserById(any(UUID.class))).thenReturn(new MsgUser());
        Message actualResult = messageService.createMessage(MOCK_MESSAGE_REQUEST);
        assertInstanceOf(Message.class, actualResult);
        assertEquals(MOCK_MESSAGE_CONTENT, actualResult.getContent());
    }

    @Test
    void shouldReturn_NotFound_WhenUserDoesntExist() {
        when(messageRepository.save(any(Message.class))).thenReturn(MOCK_MESSAGE_1);
        when(userService.getUserById(any(UUID.class))).thenThrow();
        assertThrows(EntityNotFoundException.class, () -> messageService.createMessage(MOCK_MESSAGE_REQUEST));
    }

    @Test
    void shouldReturn_ListSize1_PageOfTypeMessage_And_UpdateReadToTrue() {
        when(messageRepository.findAllByReadFalseAndReceiverIdOrderByDateSentDesc(MOCK_USER_ID, pageRequest))
                .thenReturn(new PageImpl<>(List.of(MOCK_MESSAGE_1)));
        int expectedSize = 1;
        Page<Message> actualResult = messageService.getMessagesByReceiverId(
                MOCK_USER_ID,
                true,
                0,
                10);
        assertEquals(expectedSize, actualResult.getSize());
        assertInstanceOf(Message.class, actualResult.get().toList().getFirst());
        assertTrue(actualResult.get().toList().getFirst().isRead());
    }

    @Test
    void shouldReturn_EmptyList_WhenNewMessagesNotFound() {
        when(messageRepository.findAllByReadFalseAndReceiverIdOrderByDateSentDesc(MOCK_USER_ID, pageRequest))
                .thenReturn(new PageImpl<>(List.of()));
        int expectedSize = 0;
        Page<Message> actualResult = messageService.getMessagesByReceiverId(
                MOCK_USER_ID,
                true,
                0,
                10);
        assertEquals(expectedSize, actualResult.get().toList().size());
    }

    @Test
    void shouldReturn_OrderedMessages_LatestFirst_EarliestLast() {
        when(messageRepository.findAllByReceiverIdOrderByDateSentDesc(MOCK_USER_ID, pageRequest))
                .thenReturn(new PageImpl<>(List.of(MOCK_MESSAGE_3, MOCK_MESSAGE_2, MOCK_MESSAGE_1)));
        Page<Message> actualResult = messageService.getMessagesByReceiverId(
                MOCK_USER_ID,
                false,
                0,
                10);
        List<Message> actualResultList = actualResult.get().toList();
        assertTrue(actualResultList.getFirst().getDateSent().isAfter(actualResultList.get(1).getDateSent()));
        assertTrue(actualResultList.getFirst().getDateSent().isAfter(actualResultList.get(2).getDateSent()));
        assertTrue(actualResultList.get(1).getDateSent().isAfter(actualResultList.get(2).getDateSent()));
    }
}