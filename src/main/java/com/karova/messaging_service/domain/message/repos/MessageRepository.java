package com.karova.messaging_service.domain.message.repos;

import com.karova.messaging_service.domain.message.models.Message;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends ListCrudRepository<Message, UUID> {
    Optional<List<Message>> findAllByReadFalseAndReceiverIdOrderByDateSentDesc(UUID receiverId);
    Optional<List<Message>> findAllByReceiverIdOrderByDateSentDesc(UUID receiverId);
}
