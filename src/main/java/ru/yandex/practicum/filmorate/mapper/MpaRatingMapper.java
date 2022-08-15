package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaRatingMapper implements RowMapper<MpaRating> {

    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaRating mpaRating = new MpaRating();

        mpaRating.setId(rs.getLong("mpa_id"));
        mpaRating.setName(rs.getString("mpa_name"));
        mpaRating.setDescription(rs.getString("description"));
        return mpaRating;
    }
}
