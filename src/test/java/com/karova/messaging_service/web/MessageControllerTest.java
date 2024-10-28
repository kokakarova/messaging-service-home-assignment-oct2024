package com.karova.messaging_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.services.MessageService;
import com.karova.messaging_service.domain.msguser.models.MsgUser;
import com.karova.messaging_service.web.dtos.MessageResDto;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.karova.messaging_service.web.MsgValidator.isValid;
import static com.karova.messaging_service.web.dtos.MessageResDto.toDto;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @MockBean
    private MessageService messageService;
    @Autowired
    MockMvc mockMvc;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_URL = "/api/messages";
    private static final String INVALID_USER_ID = "6bd3ade7-7daa-4bc7-ba33-3e5879865a";
    private static final String MOCK_SENDER_ID = "2f3197d6-f0d9-480a-9781-1588012e3e73";
    private static final String MOCK_RECEIVER_ID = "7f3191d6-f0d9-480a-9781-1588012e3e55";
    private static final String MOCK_MESSAGE_CONTENT = "mock message content";
    private static final UUID MOCK_MESSAGE_ID = UUID.randomUUID();
    private static final MsgUser MOCK_SENDER = new MsgUser(UUID.fromString(MOCK_SENDER_ID), "mock-user-1", "mock1@email.com");
    private static final MsgUser MOCK_RECEIVER = new MsgUser(UUID.fromString(MOCK_RECEIVER_ID), "mock-user-2", "mock2@email.com");
    private static final Message MOCK_MESSAGE = new Message(
            MOCK_MESSAGE_ID,
            MOCK_SENDER,
            MOCK_RECEIVER,
            MOCK_MESSAGE_CONTENT,
            LocalDateTime.now(),
            true);

    MockedStatic<MessageResDto> mocked;
    @BeforeEach
    void setUp() {
        mocked = mockStatic(MessageResDto.class);
    }

    @AfterEach
    void tearDown() {
        mocked.close();
    }

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenInvalidUserId() {
        SaveMessageReqDto mockInvalidReqBody = new SaveMessageReqDto(INVALID_USER_ID, MOCK_RECEIVER_ID, MOCK_MESSAGE_CONTENT);
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/new")
                .toUriString();
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenMsgContent_IsBlank() {
        SaveMessageReqDto mockInvalidReqBody = new SaveMessageReqDto(MOCK_SENDER_ID, MOCK_RECEIVER_ID, " ");
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/new")
                .toUriString();
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenMsgContent_Missing() {
        InvalidSaveMessageDto mockInvalidReqBody = new InvalidSaveMessageDto(MOCK_SENDER_ID, MOCK_RECEIVER_ID);
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/new")
                .toUriString();
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void shouldReturn_MockMessageId_WhenStatus_200OK() {
        SaveMessageReqDto mockValidReqBody = new SaveMessageReqDto(MOCK_SENDER_ID, MOCK_RECEIVER_ID, MOCK_MESSAGE_CONTENT);
        when(messageService.createMessage(mockValidReqBody))
                .thenReturn(new Message(MOCK_MESSAGE_ID, new MsgUser(), new MsgUser(), MOCK_MESSAGE_CONTENT, LocalDateTime.now(), false));
        String json = objectMapper.writeValueAsString(mockValidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/new")
                .toUriString();
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId", Matchers.is(MOCK_MESSAGE_ID.toString())));
    }

    @Test
    @SneakyThrows
    void shouldReturn_NotFound_WhenUserNotFound() {
        SaveMessageReqDto mockValidReqBody = new SaveMessageReqDto(MOCK_SENDER_ID, MOCK_RECEIVER_ID, MOCK_MESSAGE_CONTENT);
        when(messageService.createMessage(mockValidReqBody))
                .thenThrow(EntityNotFoundException.class);
        String json = objectMapper.writeValueAsString(mockValidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/new")
                .toUriString();
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenGettingMessages_ForInvalidUserId() {
        // todo: put in beforeEach, close AfterEach
        MockedStatic<MsgValidator> mocked = mockStatic(MsgValidator.class);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/" + INVALID_USER_ID)
                .toUriString();
        mocked.when(() -> isValid(anyString()))
                .thenReturn(false);
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
        mocked.close();
    }

    @Test
    @SneakyThrows
    void shouldReturn_200OK_And_0ForPage_10ForPageSize_1ForTotalElements() {
        when(messageService.getMessagesByReceiverId(any(UUID.class), any(Boolean.class), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(MOCK_MESSAGE)));
        mocked.when(() -> toDto(MOCK_MESSAGE))
                .thenReturn(new MessageResDto(
                        MOCK_MESSAGE_ID.toString(),
                        MOCK_MESSAGE.getSender().getUserName(),
                        MOCK_MESSAGE_CONTENT,
                        MOCK_MESSAGE.getDateSent()));
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/" + MOCK_RECEIVER_ID + "?newOnly=true")
                .toUriString();
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page", Matchers.is(0)))
                .andExpect(jsonPath("$.totalPages", Matchers.is(1)))
                .andExpect(jsonPath("$.totalElements", Matchers.is(1)));
    }

    @Test
    @SneakyThrows
    void shouldReturn_200OK_And_MessagesList_Size1_WhenNewOnly_IsTrue() {
        when(messageService.getMessagesByReceiverId(any(UUID.class), any(Boolean.class), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(MOCK_MESSAGE)));
        mocked.when(() -> toDto(MOCK_MESSAGE))
                .thenReturn(new MessageResDto(
                        MOCK_MESSAGE_ID.toString(),
                        MOCK_MESSAGE.getSender().getUserName(),
                        MOCK_MESSAGE_CONTENT,
                        MOCK_MESSAGE.getDateSent()));
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/" + MOCK_RECEIVER_ID + "?newOnly=true")
                .toUriString();
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages.size()", Matchers.is(1)));
    }

    @Test
    @SneakyThrows
    void shouldReturn_200OK_And_NewMessagesList_WhenNewOnlyNotProvided() {
        when(messageService.getMessagesByReceiverId(any(UUID.class), any(Boolean.class), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(MOCK_MESSAGE)));
        mocked.when(() -> toDto(MOCK_MESSAGE))
                .thenReturn(new MessageResDto(
                        MOCK_MESSAGE_ID.toString(),
                        MOCK_MESSAGE.getSender().getUserName(),
                        MOCK_MESSAGE_CONTENT,
                        MOCK_MESSAGE.getDateSent()));
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/" + MOCK_RECEIVER_ID)
                .toUriString();
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }


    @Test
    @SneakyThrows
    void shouldReturn_EmptyList_WhenNoNewMessages() {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/" + MOCK_RECEIVER_ID + "?newOnly=true")
                .toUriString();
        when(messageService.getMessagesByReceiverId(any(UUID.class), any(Boolean.class), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of()));
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages", Matchers.empty()));
    }

    @Test
    @SneakyThrows
    void shouldReturn_200OK_AndMessageRemovedSuccessfully_WhenRemoving1Message() {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/remove?messageId=" + MOCK_MESSAGE_ID)
                .toUriString();
        Mockito.doNothing().when(messageService).deleteMessages(List.of(MOCK_MESSAGE_ID));
        mockMvc.perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is("Message removed successfully")));
    }

    @Test
    @SneakyThrows
    void shouldReturn_200OK_AndMessagesRemovedSuccessfully_WhenRemoving2Message() {
        UUID mockMessageId2 = UUID.randomUUID();
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/remove?messageId=" + MOCK_MESSAGE_ID
                + "&messageId=" + mockMessageId2)
                .toUriString();
        Mockito.doNothing().when(messageService).deleteMessages(List.of(MOCK_MESSAGE_ID));
        mockMvc.perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is("Messages removed successfully")));
    }

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenMissingMessageId() {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/remove")
                .toUriString();
        Mockito.doNothing().when(messageService).deleteMessages(List.of(MOCK_MESSAGE_ID));
        mockMvc.perform(delete(url))
                .andExpect(status().isBadRequest());
    }

}