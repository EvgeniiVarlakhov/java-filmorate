package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface LikeDao {

    int saveLikeToFilm(long idFilm, long idUser);

    Collection<Film> loadTopOfFilm(long count);

    int deleteLikeFromFilm(long filmId, long usesId);

}
