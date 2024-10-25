package com.karova.messaging_service.domain.msgrelation.models;

import com.karova.messaging_service.domain.msgrelation.RelationRole;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgRelationKey implements Serializable {
    @Column(name = "message_id")
    private UUID messageId;
    @Column(name = "user_role")
    private RelationRole userRole;
}
