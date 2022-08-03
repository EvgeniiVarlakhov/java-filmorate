package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private final String email;
    private final String login;
    private String name = "";
    private final LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long idNumber) {
        friends.add(idNumber);
    }

    public void deleteFriend(Long idNumber) {
        friends.remove(idNumber);
    }

}
