package com.karova.messaging_service.domain.message.repos;

import com.karova.messaging_service.domain.message.models.Message;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface MessageRepository extends ListCrudRepository<Message, UUID> {
}
