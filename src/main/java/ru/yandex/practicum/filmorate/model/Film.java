package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Long> likes = new HashSet<>();

    public void addLike(Long idNumber) {
        likes.add(idNumber);
    }

    public void deleteLike(Long idNumber) {
        likes.remove(idNumber);
    }

}
