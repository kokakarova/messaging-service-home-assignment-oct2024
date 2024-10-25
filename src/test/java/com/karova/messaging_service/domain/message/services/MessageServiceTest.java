package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;
import com.karova.messaging_service.domain.msgrelation.services.MsgRelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;
    @MockBean
    private MsgRelationService msgRelationService;

    private static final UUID MOCK_USER_ID = UUID.randomUUID();
    private static final String MOCK_MESSAGE_CONTENT = "mock message content";
    private static final UUID MOCK_MESSAGE_ID_1 = UUID.randomUUID();
    private static final UUID MOCK_MESSAGE_ID_2 = UUID.randomUUID();
    private static final Message MOCK_MESSAGE_1 = new Message(
            MOCK_MESSAGE_ID_1, MOCK_MESSAGE_CONTENT, LocalDateTime.now(), false
    );
    private static final Message MOCK_MESSAGE_2 = new Message(
            MOCK_MESSAGE_ID_2, MOCK_MESSAGE_CONTENT, LocalDateTime.now(), false
    );

    @Test
    void shouldReturn_ListSize2_WhenId_IsFound() {
        // mock repo
//        when(msgRelationService.getMessageRelationForReceiverId(MOCK_USER_ID))
//                .thenReturn(List.of(MOCK_MESSAGE_ID_1, MOCK_MESSAGE_ID_2));
        when(msgRelationService.getMessageRelationForReceiverId(MOCK_USER_ID))
                .thenReturn(List.of(MOCK_MESSAGE_ID_1, MOCK_MESSAGE_ID_2));
        when(messageRepository.findAllByMessageIdIn(List.of(MOCK_MESSAGE_ID_1, MOCK_MESSAGE_ID_2)))
                .thenReturn(List.of(MOCK_MESSAGE_1, MOCK_MESSAGE_2));
        int expectedSize = 2;
        List<Message> actualResult = messageService.getAllNewMessagesByReceiverId(MOCK_USER_ID);
        assertEquals(expectedSize, actualResult.size());
    }

    @Test
    void shouldReturn_Type_XXXXX_WhenId_IsFound() {

    }

    @Test
    void shouldReturn_EntityNotFound_WhenId_NotFound() {

    }
}