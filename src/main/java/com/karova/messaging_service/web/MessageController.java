package com.karova.messaging_service.web;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.services.MessageService;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import com.karova.messaging_service.web.dtos.SaveMessageResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.NullValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String hello() {
        return "Hello World";
    }
    // todo: explain and support why matching users by id (and not email, user_name etc.)
    @PostMapping("/newMessages")
    public ResponseEntity<SaveMessageResDto> saveNewMessage(@Valid @RequestBody SaveMessageReqDto messageReq) {
        Message savedMessage = messageService.createMessage(messageReq);
        SaveMessageResDto response = SaveMessageResDto.toDto(
                savedMessage.getMessageId(),
                messageReq.content(),
                messageReq.senderId(),
                messageReq.receiverId(),
                savedMessage.getSentAt());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"/newMessages", "/newMessages/{userId}"})
    public ResponseEntity<List<Message>> getNewMessages(@PathVariable Optional<String> userId) {
        if (userId.isEmpty()) {
            // fetch all new messages
            List<Message> allNewMessages = messageService.getAllNewMessages();
            // todo: convert to dto (leave out fetched: true/false)
            return ResponseEntity.ok(allNewMessages);
        } else if (!MsgValidator.isValid(userId.get())) {
            return ResponseEntity.badRequest().build();
        } else {
            // fetch new messages for user_id
            List<Message> allNewMessagesForUser = messageService.getAllNewMessagesByReceiverId(UUID.fromString(userId.get()));
            // todo: convert to dto (leave out fetched: true/false)
            return ResponseEntity.accepted().build();
        }
    }
}
