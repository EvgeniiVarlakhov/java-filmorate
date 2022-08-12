package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.InvalidValidationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidValidationException(final InvalidValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        log.info("500 {}", e.getMessage(),e);
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

}
