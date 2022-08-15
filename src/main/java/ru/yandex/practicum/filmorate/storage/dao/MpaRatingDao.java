package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

public interface MpaRatingDao {

    Collection<MpaRating> loadAllMpaRating();

    Optional<MpaRating> loadMpaRatingById(long id);

}
