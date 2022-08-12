package ru.yandex.practicum.filmorate.storage.daoStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;

import java.util.*;

@Component
public class GenreDaoStorage implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> loadAllGenre() {
        return jdbcTemplate.query("select * from GENRE", new GenreMapper());
    }

    @Override
    public Optional<Genre> loadGenreById(long id) {
        final List<Genre> genres
                = jdbcTemplate.query("select * from GENRE where GENRE_ID = ?", new GenreMapper(), id);

        if (genres.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(genres.get(0));
    }

    @Override
    public Collection<Genre> loadGenreIntoFilm(long filmId) {
        String sqlQuery = "select g.* from GENRE g join FILMS_GENRE FG on g.GENRE_ID = FG.GENRE_ID where FILM_ID =?";
        return jdbcTemplate.query(sqlQuery, new GenreMapper(), filmId);
    }

    @Override
    public void saveFilmGenre(Film film) {
        deleteFilmGenre(film.getId());
        String sqlQuery = "insert into FILMS_GENRE (FILM_ID, GENRE_ID) values (?,?)";
        final List<Genre> genres = new ArrayList<>(film.getGenres());
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    @Override
    public void deleteFilmGenre(long filmId) {
        String sqlQuery = "delete from FILMS_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
