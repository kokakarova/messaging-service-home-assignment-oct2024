package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.dtos.FetchNewMessagesDto;
import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;

import com.karova.messaging_service.domain.user.services.MsgUserService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MsgUserService msgUserService;

    // todo: tests
    public Message createMessage(SaveMessageReqDto message) {
        return messageRepository.save(
                new Message(UUID.randomUUID(),
                        msgUserService.getUserById(message.senderId()),
                        msgUserService.getUserById(message.receiverId()),
                        message.content(),
                        LocalDateTime.now(), false));
    }

    public List<Message> getAllNewMessagesByReceiverId(UUID receiverId) {
        List<Message> allNewMessages = messageRepository.findAllByReadFalseAndReceiverId(receiverId)
                .orElse(new ArrayList<>());
        allNewMessages.forEach(m -> {
            m.setRead(true);
            messageRepository.save(m);
        });
        return allNewMessages;
    }

    public List<Message> getAllMessagesByReceiverId(UUID receiverId) {
        List<Message> allMessages = messageRepository.findAllByReceiverIdOrderByDateSentDesc(receiverId)
                .orElse(new ArrayList<>());
        allMessages.forEach(m -> {
            if (!m.isRead()) {
                // todo: put in helper method. private, test for private
                m.setRead(true);
                messageRepository.save(m);
            }
        });
        return allMessages;
    }

    public void deleteMessages(List<UUID> messageIds) {
        messageRepository.deleteAllById(messageIds);
    }
}
