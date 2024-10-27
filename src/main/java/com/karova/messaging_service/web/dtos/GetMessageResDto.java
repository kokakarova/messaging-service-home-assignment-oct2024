package com.karova.messaging_service.web.dtos;

import com.karova.messaging_service.domain.message.models.Message;

import java.time.LocalDateTime;

public record GetMessageResDto(
        String messageId,
        String senderName,
        String content,
        LocalDateTime sentAt
) {
    public static GetMessageResDto toDto(Message message) {
        return new GetMessageResDto(
                message.getId().toString(),
                message.getSender().getUserName(),
                message.getContent(),
                message.getDateSent());
    }
}
