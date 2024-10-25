package com.karova.messaging_service.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.services.MessageService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @MockBean
    private MessageService messageService;
    @Autowired
    MockMvc mockMvc;
    private static final String BASE_URL = "/api";
    private static final String INVALID_USER_ID = "6bd3ade7-7daa-4bc7-ba33-3e5879865a";
    private static final String VALID_SENDER_ID = "2f3197d6-f0d9-480a-9781-1588012e3e73";
    private static final String VALID_RECEIVER_ID = "7f3191d6-f0d9-480a-9781-1588012e3e55";
    private static final String MOCK_MESSAGE_CONTENT = "mock message content";
    private static final UUID MOCK_MESSAGE_ID = UUID.randomUUID();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenInvalidUserId() {
        SaveMessageReqDto mockInvalidReqBody = new SaveMessageReqDto(INVALID_USER_ID, VALID_RECEIVER_ID, MOCK_MESSAGE_CONTENT);
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages")
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
        SaveMessageReqDto mockInvalidReqBody = new SaveMessageReqDto(VALID_SENDER_ID, VALID_RECEIVER_ID, " ");
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages")
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
        InvalidSaveMessageDto mockInvalidReqBody = new InvalidSaveMessageDto(VALID_SENDER_ID, VALID_RECEIVER_ID);
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages")
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
        SaveMessageReqDto mockValidReqBody = new SaveMessageReqDto(VALID_SENDER_ID, VALID_RECEIVER_ID, MOCK_MESSAGE_CONTENT);
        when(messageService.createMessage(mockValidReqBody))
                .thenReturn(new Message(MOCK_MESSAGE_ID, MOCK_MESSAGE_CONTENT, LocalDateTime.now(), false));
        String json = objectMapper.writeValueAsString(mockValidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages")
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
    void shouldReturn_BadRequest_WhenUserIdPresentButInvalid() {
        String invalidUserId = "invalid-user-id";
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages/" + invalidUserId )
                .toUriString();
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    @SneakyThrows
//    void shouldReturn_200OK_AND_MessagesListForUser_WhenUserIdPresentAndValid() {
//        String validUserId = "2f3197d6-f0d9-480a-9781-1588012e3e73";
//        when(messageService.getAllNewMessagesByReceiverId())
//                .thenReturn(List.of(new Message(MOCK_MESSAGE_ID, MOCK_MESSAGE_CONTENT, LocalDateTime.now(), true)));
//        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages/" + validUserId )
//                .toUriString();
//        mockMvc.perform(get(url))
//                .andExpect(status().isOk())
//                .andExpectAll(jsonPath("$[0].receiverId", Matchers.hasSize(1)));
//    }

    @Test
    @SneakyThrows
    void shouldReturn_ListOfNemMessages_WhenNoUserId() {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages" )
                .toUriString();
        when(messageService.getAllNewMessages()).thenReturn(List.of(new Message(MOCK_MESSAGE_ID, MOCK_MESSAGE_CONTENT, LocalDateTime.now(), false)));
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].messageId", Matchers.is(MOCK_MESSAGE_ID.toString())));
    }

//    @Test
//    @SneakyThrows
//    void shouldReturn_NotFound_WhenUserNotFound() {
//        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessages" )
//                .toUriString();
//        when(messageService.getAllNewMessages()).thenReturn(List.of(new Message(MOCK_MESSAGE_ID, MOCK_MESSAGE_CONTENT, LocalDateTime.now(), false)));
//        mockMvc.perform(get(url))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()", Matchers.is(1)))
//                .andExpect(jsonPath("$[0].messageId", Matchers.is(MOCK_MESSAGE_ID.toString())));
//    }
}