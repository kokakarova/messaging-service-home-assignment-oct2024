package com.karova.messaging_service.web;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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
    private static final String VALID_USER_ID_1 = "2f3197d6-f0d9-480a-9781-1588012e3e73";
    private static final String VALID_USER_ID_2 = "7f3191d6-f0d9-480a-9781-1588012e3e55";
    private static final String MOCK_MESSAGE_CONTENT = "mock message content";
    private static final UUID MOCK_MESSAGE_ID = UUID.randomUUID();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SneakyThrows
    void shouldReturn_BadRequest_WhenInvalidUserId() {
        SaveMessageReqDto mockInvalidReqBody = new SaveMessageReqDto(INVALID_USER_ID, VALID_USER_ID_1, MOCK_MESSAGE_CONTENT);
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessage")
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
        SaveMessageReqDto mockInvalidReqBody = new SaveMessageReqDto(VALID_USER_ID_1, VALID_USER_ID_2, " ");
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessage")
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
        InvalidSaveMessageDto mockInvalidReqBody = new InvalidSaveMessageDto(VALID_USER_ID_1, VALID_USER_ID_2);
        String json = objectMapper.writeValueAsString(mockInvalidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessage")
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
        SaveMessageReqDto mockValidReqBody = new SaveMessageReqDto(VALID_USER_ID_1, VALID_USER_ID_2, MOCK_MESSAGE_CONTENT);
        when(messageService.createMessage(mockValidReqBody))
                .thenReturn(new Message(MOCK_MESSAGE_ID, MOCK_MESSAGE_CONTENT, LocalDateTime.now()));
        String json = objectMapper.writeValueAsString(mockValidReqBody);
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/newMessage")
                .toUriString();
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message_id", Matchers.is(MOCK_MESSAGE_ID.toString())));
    }
}