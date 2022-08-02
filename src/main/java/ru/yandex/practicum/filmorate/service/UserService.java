package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidValidationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getUsersList().values();
    }

    public User createUser(User user) throws InvalidValidationException {
        validateUser(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        User newUser = userStorage.addUser(user);
        log.info("Пользователь ID-'{}' добавлен.'{}'", newUser.getId(), newUser);
        return newUser;
    }

    public User getUserById(Long idUser) {
        if (!userStorage.getUsersList().containsKey(idUser)) {
            log.error("Ошибка при получении пользователя: пользователя с таким ID не существует. '{}'", idUser);
            throw new ObjectNotFoundException("Пользователь с таким ID не существует.");
        }
        return userStorage.getUsersList().get(idUser);
    }

    public User updateUser(User user) throws ObjectNotFoundException {
        if (!userStorage.getUsersList().containsKey(user.getId())) {
            log.error("Ошибка при обновлении пользователя: пользователя с таким ID не существует. '{}'", user.getId());
            throw new ObjectNotFoundException("Пользователь с таким ID не существует.");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь ID-'{}' обновлен. '{}'", user.getId(), user);
        return userStorage.overwriteUser(user);
    }

    public Collection<User> getListOfFriends(Long idUser) {
        Collection<User> friendsByUser = new ArrayList<>();
        for (Long id : userStorage.getUsersList().get(idUser).getFriends()) {
            friendsByUser.add(userStorage.getUsersList().get(id));
        }
        return friendsByUser;
    }

    public void addFriend(Long idUser, Long idFriend) {
        if (!userStorage.getUsersList().containsKey(idUser) || !userStorage.getUsersList().containsKey(idFriend)) {
            log.error(
                    "Ошибка добовления в друзья: пользователя с таким ID не существует. '{}'или '{}'", idUser, idFriend);
            throw new ObjectNotFoundException("Пользователь с таким ID не существует.");
        }
        userStorage.getUsersList().get(idUser).addFriend(idFriend);
        userStorage.getUsersList().get(idFriend).addFriend(idUser);
    }

    public void deleteFriend(Long idUser, Long idFriend) {
        userStorage.getUsersList().get(idUser).deleteFriend(idFriend);
        userStorage.getUsersList().get(idFriend).deleteFriend(idUser);
    }

    public Collection<User> getSameFriend(Long idUser, Long idOtherUser) {
        List<User> sameUsers = new ArrayList<>();
        for (Long id : userStorage.getUsersList().get(idUser).getFriends()) {
            if (userStorage.getUsersList().get(idOtherUser).getFriends().contains(id)) {
                sameUsers.add(userStorage.getUsersList().get(id));
            }
        }
        return sameUsers;
    }

    private void validateUser(User user) throws InvalidValidationException {
        if (user.getEmail().isEmpty()) {
            log.error("Ошибка при создании пользователя: email пустой.'{}'", user.getEmail());
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
