package com.karova.messaging_service.domain.message.services;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.repos.MessageRepository;

import com.karova.messaging_service.domain.user.models.MsgUser;
import com.karova.messaging_service.domain.user.services.MsgUserService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MsgUserService msgUserService;

    public Message createMessage(SaveMessageReqDto message) throws EntityNotFoundException {
        MsgUser sender;
        MsgUser receiver;
        try {
            sender = msgUserService.getUserById(message.senderId());
            receiver = msgUserService.getUserById(message.receiverId());
        } catch (Exception e) {
            throw new EntityNotFoundException("User not found");
        }
        return messageRepository.save(
                new Message(UUID.randomUUID(),
                        sender,
                        receiver,
                        message.content(),
                        LocalDateTime.now(),
                        false));
    }

    public Page<Message> getMessagesByReceiverId(UUID receiverId, boolean newOnly, int page, int pageSize) {
        Page<Message> messages;
        Pageable pageRequest = PageRequest.of(page, pageSize);
        if (newOnly) {
            messages = messageRepository.findAllByReadFalseAndReceiverIdOrderByDateSentDesc(receiverId, pageRequest);
        } else {
            messages = messageRepository.findAllByReceiverIdOrderByDateSentDesc(receiverId, pageRequest);
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
