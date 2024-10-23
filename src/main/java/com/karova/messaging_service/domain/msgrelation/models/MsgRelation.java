package com.karova.messaging_service.domain.msgrelation.models;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.msgrelation.RelationRole;
import com.karova.messaging_service.domain.user.models.MsgUser;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class MsgRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID relationshipId;
    private RelationRole userRole;
    @OneToOne
    private MsgUser user;
    @OneToOne
    private Message message;
}
