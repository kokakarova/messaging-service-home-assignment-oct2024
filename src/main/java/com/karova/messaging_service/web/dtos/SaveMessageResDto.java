package com.karova.messaging_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record SaveMessageResDto(
        @JsonProperty("message_id")
        String msgId,
        @JsonProperty("message_content")
        String msgContent,
        @JsonProperty("sender_id")
        String senderId,
        @JsonProperty("receiver_id")
        String receiverId,
        @JsonProperty("sent_at")
        LocalDateTime sentAt) {

    public static SaveMessageResDto toDto(UUID savedMessageId,
                                          String content,
                                          String senderId,
                                          String receiverId,
                                          LocalDateTime sentAt) {
        return new SaveMessageResDto(
                savedMessageId.toString(),
                content,
                senderId,
                receiverId,
                sentAt
        );
    }
}
