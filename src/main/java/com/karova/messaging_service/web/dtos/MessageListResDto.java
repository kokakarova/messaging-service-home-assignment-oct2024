package com.karova.messaging_service.web.dtos;

import java.util.List;

public record MessageListResDto(
        int page,
        int totalPages,
        int totalElements,
        List<MessageResDto> messages
) {
}
