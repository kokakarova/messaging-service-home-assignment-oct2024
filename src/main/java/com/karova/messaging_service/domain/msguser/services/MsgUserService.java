package com.karova.messaging_service.domain.msguser.services;

import com.karova.messaging_service.domain.msguser.models.MsgUser;
import com.karova.messaging_service.domain.msguser.repos.MsgUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MsgUserService {
    private final MsgUserRepository msgUserRepository;

    public MsgUser getUserById(UUID userId) throws NoSuchElementException {
        return msgUserRepository.findById(userId).orElseThrow();
    }

}
