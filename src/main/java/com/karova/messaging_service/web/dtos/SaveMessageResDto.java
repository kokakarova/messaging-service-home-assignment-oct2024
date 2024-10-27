package com.karova.messaging_service.web.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record SaveMessageResDto(
        String messageId,
        String messageContent,
        String senderId,
        String receiverId,
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
