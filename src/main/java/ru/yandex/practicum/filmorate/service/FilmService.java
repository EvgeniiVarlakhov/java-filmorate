package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.exception.InvalidValidationException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class FilmService {
    private static final LocalDate VALID_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final MpaRatingDao mpaRatingDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;

    @Autowired
    public FilmService(FilmStorage filmStorage, MpaRatingDao mpaRatingDao, GenreDao genreDao, LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.mpaRatingDao = mpaRatingDao;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
    }

    public Collection<Film> getAllFilms() {
        List<Film> films = new ArrayList<>(filmStorage.getFilmsList());
        for (Film film : films) {
            film.setGenres(new HashSet<>(genreDao.loadGenreIntoFilm(film.getId())));
        }
        return films;
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        Film newFilm = filmStorage.addFilm(film);
        genreDao.saveFilmGenre(film);
        log.info("Добавлен новый фильм: {}", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        if (filmStorage.overwriteFilm(film) < 1) {
            log.error("Ошибка при обновлении фильма. Фильма с таким ID не существует. '{}'", film.getId());
            throw new ObjectNotFoundException("Фильма с таким ID не существует.");
        }
        genreDao.saveFilmGenre(film);
        log.info("Фильм ID-{} обновлен. {}", film.getId(), film);
        return film;
    }

    public void addLikeToFilm(long idFilm, long idUser) {
        if (likeDao.saveLikeToFilm(idFilm, idUser) < 1) {
            log.error("Ошибка при добавлении лайка. Фильма или пользователя с таким ID не существует. '{}', '{}'",
                    idFilm, idUser);
            throw new ObjectNotFoundException("Фильма с таким ID не существует.");
        }
    }

    public void deleteLikeToFilm(long idFilm, long idUser) {
        if (likeDao.deleteLikeFromFilm(idFilm, idUser) == 0) {
            log.error("Ошибка при удалении лайка: лайка не существует. Фильм '{}', Пользователь '{}'", idFilm, idUser);
            throw new ObjectNotFoundException("Лайка не существует.");
        }
    }

    public Film getFilmById(long idFilm) {
        Optional<Film> filmById = filmStorage.getFilmById(idFilm);
        if (filmById.isEmpty()) {
            log.error("Ошибка при получении фильма: фильма с таким ID не существует. '{}'", idFilm);
            throw new ObjectNotFoundException("Фильм с таким ID не существует.");
        }
        filmById.get().setGenres(new HashSet<>(genreDao.loadGenreIntoFilm(idFilm)));
        return filmById.get();
    }

    public Collection<Film> getTopFilm(long size) {
        return likeDao.loadTopOfFilm(size);
    }


    public Collection<MpaRating> getListOfMpaRating() {
        return mpaRatingDao.loadAllMpaRating();
    }

    public MpaRating getMpaRatingById(long id) {
        Optional<MpaRating> mpaById = mpaRatingDao.loadMpaRatingById(id);
        if (mpaById.isEmpty()) {
            log.error("Ошибка при получении MPA: MPA с таким ID не существует. '{}'", id);
            throw new ObjectNotFoundException("MPA с таким ID не существует.");
        }
        return mpaById.get();
    }

    public Collection<Genre> getListOfGenre() {
        return genreDao.loadAllGenre();
    }

    public Genre getGenreById(long id) {
        Optional<Genre> genreId = genreDao.loadGenreById(id);
        if (genreId.isEmpty()) {
            log.error("Ошибка при получении Genre: Genre с таким ID не существует. '{}'", id);
            throw new ObjectNotFoundException("Genre с таким ID не существует.");
        }
        return genreId.get();
    }

    private void validateFilm(Film film) throws InvalidValidationException {
        if (film.getName().isEmpty()) {
            log.error("Ошибка при создании фильма: название фильма не указано. '{}'", film.getName());
            throw new InvalidValidationException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(VALID_DATE)) {
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

        if (film.getMpa() == null) {
            log.error("Ошибка при создании фильма: длина описания более 200 символов. '{}'", film.getMpa());
            throw new InvalidValidationException("Рейтинг МРА не может быть NULL.");
        }
    }

}
