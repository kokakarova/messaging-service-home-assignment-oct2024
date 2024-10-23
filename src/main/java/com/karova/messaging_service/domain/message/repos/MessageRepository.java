package com.karova.messaging_service.domain.message.repos;

import com.karova.messaging_service.domain.message.models.Message;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends ListCrudRepository<Message, UUID> {
}
