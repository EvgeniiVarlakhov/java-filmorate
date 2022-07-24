package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private long idNumber = 1;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllFilms() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        validated(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(idNumber);
        users.put(idNumber, user);
        log.info("Пользователь ID-'{}' добавлен.'{}'", user.getId(), user);
        idNumber++;
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("Ошибка при обновлении пользователя: пользователя с таким ID не существует.'{}'", user.getId());
            throw new ValidationException("Пользователь с таким ID не существует.");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Пользователь ID-'{}' обновлен.'{}'", user.getId(), user);
        return user;
    }

    private void validated(User user) throws ValidationException {
        if (user.getEmail().isEmpty()) {
            log.error("Ошибка при создании пользователя: email пустой.'{}'", user.getEmail());
            throw new ValidationException("email не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Ошибка при создании пользователя: email не содержит @. '{}'", user.getEmail());
            throw new ValidationException("email не содержит @.");
        }
        if (user.getLogin().isBlank()) {
            log.error("Ошибка при создании пользователя: поле login пусто. '{}'", user.getLogin());
            throw new ValidationException("Поле login не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Ошибка при создании пользователя: поле login содержит пробелы. '{}'", user.getLogin());
            throw new ValidationException("Поле login не должно содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка при создании пользователя: дата рождения указана в будущем. '{}'", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}
