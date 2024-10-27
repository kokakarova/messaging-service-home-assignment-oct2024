package com.karova.messaging_service.exceptions;

public record CustomError(int status, String reasonPhrase, String message) {
}
