package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.InvalidValidationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendDao friendDao;

    @Autowired
    public UserService(UserStorage userStorage, FriendDao friendDao) {
        this.userStorage = userStorage;
        this.friendDao = friendDao;
    }

    public Collection<User> getAllUsers() {
        return userStorage.loadAllUsers();
    }

    public User createUser(User user) throws InvalidValidationException {
        validateUser(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        User newUser = userStorage.addUser(user);
        log.info("Пользователь ID-'{}' добавлен. '{}'", newUser.getId(), newUser);
        return newUser;
    }

    public User getUserById(Long idUser) {
        Optional<User> userById = userStorage.loadUserById(idUser);
        if (userById.isEmpty()) {
            log.error("Ошибка при получении пользователя: пользователя с таким ID не существует. '{}'", idUser);
            throw new ObjectNotFoundException("Пользователь с таким ID не существует.");
        }
        return userById.get();
    }

    public User updateUser(User user) throws ObjectNotFoundException {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (userStorage.overwriteUser(user) < 1) {
            log.error("Ошибка при обновлении пользователя: пользователя с таким ID не существует. '{}'", user.getId());
            throw new ObjectNotFoundException("Пользователь с таким ID не существует.");
        }
        log.info("Пользователь ID-'{}' обновлен. '{}'", user.getId(), user);
        return user;
    }

    public Collection<User> getListOfFriends(Long idUser) {
        return friendDao.getListOfFriends(idUser);
    }

    public void addFriend(Long idUser, Long idFriend) {
        if (userStorage.loadUserById(idUser).isEmpty() || userStorage.loadUserById(idFriend).isEmpty()) {
            log.error(
                    "Ошибка добовления в друзья: пользователя с таким ID не существует. '{}' или '{}'",
                    idUser, idFriend);
            throw new ObjectNotFoundException("Пользователь с таким ID не существует.");
        }
        friendDao.addFriend(idUser, idFriend);
    }

    public void deleteFriend(Long idUser, Long idFriend) {
        friendDao.deleteFriend(idUser, idFriend);
    }

    public Collection<User> getSameFriend(Long idUser, Long idOtherUser) {
        return friendDao.getSameFriend(idUser, idOtherUser);
    }

    private void validateUser(User user) throws InvalidValidationException {
        if (user.getEmail().isEmpty()) {
            log.error("Ошибка при создании пользователя: email пустой. '{}' ", user.getEmail());
            throw new InvalidValidationException("email не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Ошибка при создании пользователя: email не содержит @. '{}'", user.getEmail());
            throw new InvalidValidationException("email не содержит @.");
        }
        if (user.getLogin().isBlank()) {
            log.error("Ошибка при создании пользователя: поле login пусто. '{}'", user.getLogin());
            throw new InvalidValidationException("Поле login не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Ошибка при создании пользователя: поле login содержит пробелы. '{}'", user.getLogin());
            throw new InvalidValidationException("Поле login не должно содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка при создании пользователя: дата рождения указана в будущем. '{}'", user.getBirthday());
            throw new InvalidValidationException("Дата рождения не может быть в будущем.");
        }
    }

}
