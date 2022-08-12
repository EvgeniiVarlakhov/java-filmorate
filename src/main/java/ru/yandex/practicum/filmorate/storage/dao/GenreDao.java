package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreDao {

    Collection<Genre> loadAllGenre();

    Optional<Genre> loadGenreById(long id);

    Collection<Genre> loadGenreIntoFilm(long filmId);

    void saveFilmGenre(Film film);

    void deleteFilmGenre(long filmId);

}
