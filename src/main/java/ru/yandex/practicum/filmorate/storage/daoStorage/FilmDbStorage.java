package ru.yandex.practicum.filmorate.storage.daoStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery
                = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING) values (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setObject(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public void removeFilm(long filmId) {
        String sqlQuery = "delete from FILMS where FILM_ID = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public int overwriteFilm(Film film) {
        String sqlQuery = "update FILMS " +
                "set FILM_ID=?, FILM_NAME =?, DESCRIPTION =?, RELEASE_DATE =?, DURATION =?, MPA_RATING =? " +
                "where FILM_ID =?";

        return jdbcTemplate.update(
                sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public Collection<Film> getFilmsList() {
        return jdbcTemplate.query("select * " +
                        "from FILMS " +
                        "join MPA_RATING MR on MR.MPA_ID = FILMS.MPA_RATING",
                new FilmMapper());
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        final List<Film> films
                = jdbcTemplate.query("select * " +
                        "from FILMS " +
                        "join MPA_RATING MR on MR.MPA_ID = FILMS.MPA_RATING " +
                        "where FILM_ID = ?",
                new FilmMapper(),
                filmId);

        if (films.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(films.get(0));
    }

}
