package com.karova.messaging_service.domain.msgrelation.models;

import com.karova.messaging_service.domain.user.models.MsgUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgRelation {
    // this entity is to keep track of sender and receiver
    @EmbeddedId
    private MsgRelationKey msgRelationKey;
    @OneToOne
    private MsgUser user;

}
