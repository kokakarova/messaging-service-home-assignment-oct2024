package com.karova.messaging_service.domain.msguser.services;

import com.karova.messaging_service.domain.msguser.models.MsgUser;
import com.karova.messaging_service.domain.msguser.repos.MsgUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MsgUserServiceTest {
    @Autowired
    private MsgUserService msgUserService;
    @MockBean
    private MsgUserRepository mockRepo;
    private static final UUID MOCK_USER_ID = UUID.randomUUID();
    private static final MsgUser MOCK_USER = new MsgUser(MOCK_USER_ID, "mock1@email.com", "mock1_user_name");

    @Test
    void shouldReturn_MsgUser_WhenFound() {
        when(mockRepo.findById(any(UUID.class))).thenReturn(Optional.of(MOCK_USER));
        MsgUser actualResult = msgUserService.getUserById(MOCK_USER_ID);
        assertInstanceOf(MsgUser.class, actualResult);
        assertEquals(MOCK_USER_ID, actualResult.getId());
    }
    @Test
    void shouldThrow_WhenUserNotFound() {
        when(mockRepo.findById(any(UUID.class))).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> msgUserService.getUserById(MOCK_USER_ID));
    }
}