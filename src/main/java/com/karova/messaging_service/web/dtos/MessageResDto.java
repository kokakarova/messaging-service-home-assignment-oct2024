package com.karova.messaging_service.web.dtos;

import com.karova.messaging_service.domain.message.models.Message;

import java.time.LocalDateTime;

public record MessageResDto(
        String messageId,
        String senderName,
        String content,
        LocalDateTime sentAt
) {
    public static MessageResDto toDto(Message message) {
        return new MessageResDto(
                message.getId().toString(),
                message.getSender().getUserName(),
                message.getContent(),
                message.getDateSent());
    }
}
