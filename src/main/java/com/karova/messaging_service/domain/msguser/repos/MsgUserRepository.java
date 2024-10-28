package com.karova.messaging_service.domain.msguser.repos;

import com.karova.messaging_service.domain.msguser.models.MsgUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MsgUserRepository extends ListCrudRepository<MsgUser, UUID> {
}
