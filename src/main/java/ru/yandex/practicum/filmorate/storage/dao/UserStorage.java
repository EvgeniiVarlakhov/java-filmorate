package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);

    void deleteUser(long userId);

    Integer overwriteUser(User user);


    Optional<User> loadUserById(long userId);

    Collection<User> loadAllUsers();
}
