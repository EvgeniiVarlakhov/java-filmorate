package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    User addUser(User user);

    void deleteUser(Long userId);

    User overwriteUser(User user);

    Map<Long, User> getUsersList();

}
