package com.karova.messaging_service.domain.user.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "user")
public class MsgUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String email;
    private String userName;
}
