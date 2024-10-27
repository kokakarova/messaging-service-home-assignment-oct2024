package com.karova.messaging_service.web;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.services.MessageService;
import com.karova.messaging_service.web.dtos.GetMessageResDto;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import com.karova.messaging_service.web.dtos.SaveMessageResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String hello() {
        return "Hello World";
    }

    // todo: explain and support why matching users by id (and not email, user_name etc.)
    @PostMapping("/new")
    public ResponseEntity<SaveMessageResDto> saveNewMessage(@Valid @RequestBody SaveMessageReqDto messageReq) {
        Message savedMessage = messageService.createMessage(messageReq);
        SaveMessageResDto response = SaveMessageResDto.toDto(
                savedMessage.getId(),
                messageReq.content(),
                messageReq.senderId(),
                messageReq.receiverId(),
                savedMessage.getDateSent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/new/{userId}")
    public ResponseEntity<List<GetMessageResDto>> getNewMessages(@PathVariable String userId) {
        if (MsgValidator.isValid(userId)) {
            List<Message> allNewMessagesForUser = messageService.getAllNewMessagesByReceiverId(UUID.fromString(userId));
            List<GetMessageResDto> response = allNewMessagesForUser.stream().map(GetMessageResDto::toDto).toList();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // todo: pagination (?)
    @GetMapping("/{userId}")
    public ResponseEntity<List<GetMessageResDto>> getAllMessage(@PathVariable UUID userId) {
        if (MsgValidator.isValid(userId.toString())) {
            List<Message> allMessagesForUser = messageService.getAllMessagesByReceiverId(userId);
            List<GetMessageResDto> response = allMessagesForUser.stream().map(GetMessageResDto::toDto).toList();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // todo: elaborate why it's one endpoint for single and multiple messages
    // todo: write tests
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeMessages(@RequestParam List<UUID> messageId) {
        messageService.deleteMessages(messageId);
        return ResponseEntity.ok("Message removed successfully");
    }
}
