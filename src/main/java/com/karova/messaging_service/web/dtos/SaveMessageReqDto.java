package com.karova.messaging_service.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SaveMessageReqDto(
        @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", message="Invalid sender ID")
        @JsonProperty("sender_id")
        String senderId,
        @Pattern(regexp = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}", message="Invalid receiver ID")
        @JsonProperty("receiver_id")
        String receiverId,
        @NotBlank(message = "Message content can not be blank, empty, or null")
        String content) {
}
