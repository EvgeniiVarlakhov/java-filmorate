package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private long idNumber = 1;
    private final Map<Long, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validated(film);
        film.setId(idNumber);
        films.put(idNumber, film);
        log.info("Добавлен новый фильм: {}", film);
        idNumber++;
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Ошибка при обновлении фильма.Фильма с таким ID не существует.{}", film);
            throw new RuntimeException("Фильма с таким ID не существует.");
        }
        films.put(film.getId(), film);
        log.info("Фильм ID-{} обновлен.{}", film.getId(), film);
        return film;
    }

    private void validated(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.error("Ошибка при создании фильма: название фильма не указано.'{}'", film.getName());
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка при создании фильма: дата релиза раньше 28.12.1895. '{}'", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895.");
        }
        if (film.getDuration() < 0) {
            log.error("Ошибка при создании фильма: продолжительность фильма отрицательная. '{}'", film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка при создании фильма: длина описания более 200 символов. '{}'"
                    , film.getDescription().length());
            throw new ValidationException("Длина описания не может быть более 200 символов.");
        }
    }

}
