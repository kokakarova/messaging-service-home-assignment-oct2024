package com.karova.messaging_service.domain.message.models;

import com.karova.messaging_service.domain.user.models.MsgUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private MsgUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private MsgUser receiver;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime dateSent;

    @Column(nullable = false)
    private boolean read;
}
