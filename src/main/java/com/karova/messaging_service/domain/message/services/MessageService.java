package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;
import com.karova.messaging_service.domain.msgrelation.services.MsgRelationService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MsgRelationService msgRelationService;

    public Message createMessage(SaveMessageReqDto message) {
        Message newMessage = messageRepository.save(new Message(UUID.fromString("2f3197d6-f0d9-480a-9784-2588012e3e73"),
                message.content(), LocalDateTime.now(), false));
        msgRelationService.createNewMessageRelations(message, newMessage);
        return newMessage;
    }

    public List<Message> getAllNewMessages() {
        List<Message> allNewMessages =  messageRepository.findAllByFetchedFalse();
        allNewMessages.forEach(m -> m.setFetched(true));
        messageRepository.saveAll(allNewMessages);
        return allNewMessages;
    }
}
