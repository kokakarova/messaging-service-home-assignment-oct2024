package com.karova.messaging_service.domain.msgrelation.repos;

import com.karova.messaging_service.domain.msgrelation.models.MsgRelation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MsgRelationRepository extends ListCrudRepository<MsgRelation, UUID> {
}
