package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private long idNumber = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(idNumber);
        users.put(idNumber, user);
        User createdUser = users.get(idNumber);
        idNumber++;
        return createdUser;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public User overwriteUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public Map<Long, User> getUsersList() {
        return users;
    }

}
