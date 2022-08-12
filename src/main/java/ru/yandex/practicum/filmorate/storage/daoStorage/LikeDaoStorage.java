package ru.yandex.practicum.filmorate.storage.daoStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;

import java.util.Collection;

@Component
public class LikeDaoStorage implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int saveLikeToFilm(long idFilm, long idUser) {
        String sqlQuery = "insert into USERS_FILMS ( FILM_ID, USER_ID ) values (?,?)";
        return jdbcTemplate.update(sqlQuery, idFilm, idUser);
    }

    @Override
    public Collection<Film> loadTopOfFilm(long size) {
        return jdbcTemplate.query(
                "select f.*, MR.* from FILMS f " +
                        "left join USERS_FILMS UF on f.FILM_ID = UF.FILM_ID " +
                        "join MPA_RATING MR on MR.MPA_ID = f.MPA_RATING " +
                        "group by f.FILM_ID " +
                        "order by count(USER_ID) desc " +
                        "limit ?",
                new FilmMapper(),
                size);
    }

    @Override
    public int deleteLikeFromFilm(long filmId, long usesId) {
        String sqlQuery = "delete from USERS_FILMS where FILM_ID = ? and USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId, usesId);
    }

}
