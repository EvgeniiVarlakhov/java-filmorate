package ru.yandex.practicum.filmorate.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ValidationException extends RuntimeException {
    public ValidationException(String string) {
        super(string);
    }

}