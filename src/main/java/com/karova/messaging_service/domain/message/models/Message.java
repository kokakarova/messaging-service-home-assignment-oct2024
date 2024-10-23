package com.karova.messaging_service.domain.message.models;

import com.karova.messaging_service.domain.user.models.MsgUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID msgId;
    private String content;
    private LocalDateTime receivedAt;
}
