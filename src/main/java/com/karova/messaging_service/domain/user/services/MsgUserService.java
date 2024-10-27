package com.karova.messaging_service.domain.user.services;

import com.karova.messaging_service.domain.user.models.MsgUser;
import com.karova.messaging_service.domain.user.repos.MsgUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MsgUserService {
    private final MsgUserRepository msgUserRepository;

    public MsgUser getUserById(String s) throws NoSuchElementException {
        return msgUserRepository.findById(UUID.fromString(s)).orElseThrow();
    }

}
