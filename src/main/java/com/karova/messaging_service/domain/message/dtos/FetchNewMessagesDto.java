package com.karova.messaging_service.domain.message.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record FetchNewMessagesDto(
        String messageId,
        String content,
        UUID senderId,
        UUID receiverId,
        LocalDateTime sentAt) {
}
