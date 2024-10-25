package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.dtos.FetchNewMessagesDto;
import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;
import com.karova.messaging_service.domain.msgrelation.models.MsgRelation;
import com.karova.messaging_service.domain.msgrelation.services.MsgRelationService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MsgRelationService msgRelationService;

    // todo: tests
    public Message createMessage(SaveMessageReqDto message) {
        Message newMessage = messageRepository.save(new Message(UUID.fromString("2f3197d6-f0d9-480a-9784-2588012e3e73"),
                message.content(), LocalDateTime.now(), false));
        msgRelationService.createNewMessageRelations(message, newMessage);
        return newMessage;
    }

    // todo: tests
    public List<Message> getAllNewMessages() {
        List<Message> allNewMessages = messageRepository.findAllByFetchedFalse();
        allNewMessages.forEach(m -> m.setFetched(true));
        messageRepository.saveAll(allNewMessages);
        return allNewMessages;
    }

    public List<FetchNewMessagesDto> getAllNewMessagesByReceiverId(UUID receiverId) {
        // get msg ids where user is receiver
        List<MsgRelation> receivedMessages = msgRelationService.getMessageRelationForReceiverId(receiverId);
        Map<UUID, UUID> messageAndSender = new HashMap<>();
        receivedMessages.forEach(m -> {messageAndSender.put(m.getMsgRelationKey().getMessageId(), m.)})
//        List<UUID> receivedMessagesIds = receivedMessages.stream().map(m -> m.getMsgRelationKey().getMessageId()).toList();
        // get msg where msg id and fetch false
        // todo: (custom) exception - entity not found
        List<Message> newMessagesReceiver = messageRepository.findAllByMessageIdIn(messageAndSender.keySet().stream().toList());
        return ;
    }
}
