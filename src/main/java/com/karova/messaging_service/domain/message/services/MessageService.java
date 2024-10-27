package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;

import com.karova.messaging_service.domain.user.services.MsgUserService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
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

//    public List<Message> getAllNewMessagesByReceiverId(UUID receiverId) {
//        List<Message> allNewMessages = messageRepository.findAllByReadFalseAndReceiverId(receiverId)
//                .orElse(new ArrayList<>());
//        allNewMessages.forEach(m -> {
//            m.setRead(true);
//            messageRepository.save(m);
//        });
//        return allNewMessages;
//    }

    public List<Message> getMessagesByReceiverId(UUID receiverId, boolean newOnly) {
        List<Message> messages;
        if (newOnly) {
            messages = messageRepository.findAllByReadFalseAndReceiverIdOrderByDateSentDesc(receiverId)
                    .orElse(new ArrayList<>());
        } else {
            messages = messageRepository.findAllByReceiverIdOrderByDateSentDesc(receiverId)
                    .orElse(new ArrayList<>());
        }

        messages.forEach(m -> {
            if (!m.isRead()) {
                m.setRead(true);
                messageRepository.save(m);
            }
        });
        return messages;
    }

    public void deleteMessages(List<UUID> messageIds) {
        messageRepository.deleteAllById(messageIds);
    }
}
