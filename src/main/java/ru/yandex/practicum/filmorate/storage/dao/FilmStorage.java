package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    void removeFilm(long filmId);

    int overwriteFilm(Film film);

    Collection<Film> getFilmsList();

    Optional<Film> getFilmById(long filmId);

}
