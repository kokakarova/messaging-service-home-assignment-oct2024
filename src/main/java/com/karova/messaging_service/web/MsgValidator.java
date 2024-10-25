package com.karova.messaging_service.web;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class MsgValidator {
    public static boolean isValid(String id) {
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        return id.matches(String.valueOf(UUID_REGEX));
    }
}
