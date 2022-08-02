package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.comparator.FilmLikesComparator;
import ru.yandex.practicum.filmorate.exception.InvalidValidationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final LocalDate VALIDDATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmLikesComparator filmLikesComparator = new FilmLikesComparator();

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getFilmsList().values();
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        Film newFilm = filmStorage.addFilm(film);
        log.info("Добавлен новый фильм: {}", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.getFilmsList().containsKey(film.getId())) {
            log.error("Ошибка при обновлении фильма. Фильма с таким ID не существует. '{}'", film.getId());
            throw new ObjectNotFoundException("Фильма с таким ID не существует.");
        }
        filmStorage.overwriteFilm(film);
        log.info("Фильм ID-{} обновлен.{}", film.getId(), film);
        return film;
    }

    public void addLikeToFilm(Long idFilm, Long idUser) {
        if (!filmStorage.getFilmsList().containsKey(idFilm)) {
            log.error("Ошибка при добавлении лайка. Фильма с таким ID не существует. '{}'", idFilm);
            throw new ObjectNotFoundException("Фильма с таким ID не существует.");
        }
        if (!userStorage.getUsersList().containsKey(idUser)) {
            log.error("Ошибка при добавлении лайка.Пользователя с таким ID не существует. '{}'", idUser);
            throw new ObjectNotFoundException("Пользователя с таким ID не существует.");
        }
        filmStorage.getFilmsList().get(idFilm).addLike(idUser);
    }

    public void deleteLikeToFilm(Long idFilm, Long idUser) {
        if (!filmStorage.getFilmsList().containsKey(idFilm)) {
            log.error("Ошибка при удалении лайка. Фильма с таким ID не существует. '{}'", idFilm);
            throw new ObjectNotFoundException("Фильма с таким ID не существует.");
        }
        if (!userStorage.getUsersList().containsKey(idUser)) {
            log.error("Ошибка при удалении лайка. Пользователя с таким ID не существует. '{}'", idUser);
            throw new ObjectNotFoundException("Пользователя с таким ID не существует.");
        }
        filmStorage.getFilmsList().get(idFilm).deleteLike(idUser);
    }

    public Film getFilmById(Long idFilm) {
        if (!filmStorage.getFilmsList().containsKey(idFilm)) {
            log.error("Ошибка при получении фильма. Фильма с таким ID не существует. '{}'", idFilm);
            throw new ObjectNotFoundException("Фильма с таким ID не существует.");
        }
        return filmStorage.getFilmsList().get(idFilm);
    }

    public Collection<Film> getTopFilm(Long size) {
        List<Film> topFilm = new ArrayList<>();
        topFilm.addAll(filmStorage.getFilmsList().values());
        topFilm.sort(filmLikesComparator);
        return topFilm.stream().limit(size).collect(Collectors.toList());
    }


    private void validateFilm(Film film) throws InvalidValidationException {
        if (film.getName().isEmpty()) {
            log.error("Ошибка при создании фильма: название фильма не указано. '{}'", film.getName());
            throw new InvalidValidationException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(VALIDDATE)) {
            log.error("Ошибка при создании фильма: дата релиза раньше 28.12.1895. '{}'", film.getReleaseDate());
            throw new InvalidValidationException("Дата релиза не может быть раньше 28.12.1895.");
        }
        if (film.getDuration() < 0) {
            log.error("Ошибка при создании фильма: продолжительность фильма отрицательная. '{}'", film.getDuration());
            throw new InvalidValidationException("Продолжительность фильма не может быть отрицательной.");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка при создании фильма: длина описания более 200 символов. '{}'"
                    , film.getDescription().length());
            throw new InvalidValidationException("Длина описания не может быть более 200 символов.");
        }
    }

}
