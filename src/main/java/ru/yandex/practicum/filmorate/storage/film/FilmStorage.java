package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Film addFilm(Film film);

    void removeFilm(Long filmId);

    Film overwriteFilm(Film film);

    Map<Long, Film> getFilmsList();

}
