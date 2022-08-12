package ru.yandex.practicum.filmorate.storage.daoStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class MpaRatingDaoStorage implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaRatingDaoStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<MpaRating> loadAllMpaRating() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", new MpaRatingMapper());
    }

    @Override
    public Optional<MpaRating> loadMpaRatingById(long id) {
        final List<MpaRating> mpas
                = jdbcTemplate.query("SELECT mpa_id, mpa_name, description FROM MPA_RATING WHERE mpa_id = ?",
                new MpaRatingMapper(), id);

        if (mpas.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(mpas.get(0));
    }
}


