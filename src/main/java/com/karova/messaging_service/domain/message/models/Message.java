package com.karova.messaging_service.domain.message.models;

import com.karova.messaging_service.domain.user.models.MsgUser;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    // assuming that one message can be sent to one user
    @ManyToOne
    private MsgUser receiver;
    @ManyToOne
    private MsgUser sender;
}
