package com.karova.messaging_service.web;

import com.karova.messaging_service.domain.message.models.Message;
import com.karova.messaging_service.domain.message.services.MessageService;
import com.karova.messaging_service.web.dtos.MessageListResDto;
import com.karova.messaging_service.web.dtos.MessageResDto;
import com.karova.messaging_service.web.dtos.SaveMessageReqDto;
import com.karova.messaging_service.web.dtos.SaveMessageResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/{userId}")
    public ResponseEntity<MessageListResDto> getMessage(@PathVariable UUID userId,
                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                        @RequestParam(defaultValue = "true") boolean newOnly) {
        if (MsgValidator.isValid(userId.toString())) {
            Page<Message> messages = messageService.getMessagesByReceiverId(userId, newOnly, page, pageSize);
            MessageListResDto response = new MessageListResDto(
                    messages.getNumber(),
                    messages.getTotalPages(),
                    (int) messages.getTotalElements(),
                    messages.stream().map(MessageResDto::toDto).toList());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeMessages(@RequestParam List<UUID> messageId) {
        String responseMessage = "Messages removed successfully";
        if (messageId.size() == 1) {
            responseMessage = "Message removed successfully";
        }
        messageService.deleteMessages(messageId);
        return ResponseEntity.ok(responseMessage);
    }
}
