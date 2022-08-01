package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long idNumber = 1;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        film.setId(idNumber);
        films.put(idNumber, film);
        Film createdFilm = films.get(idNumber);
        idNumber++;
        return createdFilm;
    }

    @Override
    public void removeFilm(Long filmId) {
        films.remove(filmId);
    }

    public Film overwriteFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Map<Long, Film> getFilmsList() {
        return films;
    }

}
