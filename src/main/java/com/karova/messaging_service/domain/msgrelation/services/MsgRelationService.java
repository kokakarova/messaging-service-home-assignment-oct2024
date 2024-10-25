package com.karova.messaging_service.domain.msgrelation.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.msgrelation.models.MsgRelation;
import com.karova.messaging_service.domain.msgrelation.models.MsgRelationKey;
import com.karova.messaging_service.domain.msgrelation.repos.MsgRelationRepository;
import com.karova.messaging_service.domain.user.models.MsgUser;
import com.karova.messaging_service.domain.user.services.MsgUserService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.karova.messaging_service.domain.msgrelation.RelationRole.RECEIVER;
import static com.karova.messaging_service.domain.msgrelation.RelationRole.SENDER;

@Service
@RequiredArgsConstructor
public class MsgRelationService {
    private final MsgUserService msgUserService;
    private final MsgRelationRepository msgRelationRepository;

    public void createNewMessageRelations(SaveMessageReqDto message, Message newMessage) {
        MsgUser sender = msgUserService.getUserById(UUID.fromString(message.senderId()));
        MsgUser receiver = msgUserService.getUserById(UUID.fromString(message.receiverId()));
        MsgRelation msgRelationSender = new MsgRelation(new MsgRelationKey(newMessage.getMsgId(), SENDER), sender);
        MsgRelation msgRelationReceiver = new MsgRelation(new MsgRelationKey(newMessage.getMsgId(), RECEIVER), receiver);
        msgRelationRepository.saveAll(List.of(msgRelationSender, msgRelationReceiver));
    }
}
