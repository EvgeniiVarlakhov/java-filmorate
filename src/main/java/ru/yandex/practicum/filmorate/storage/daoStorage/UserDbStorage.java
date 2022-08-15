package ru.yandex.practicum.filmorate.storage.daoStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS ( EMAIL, LOGIN, USER_NAME, BIRTHDAY ) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public Integer overwriteUser(User user) {
        String sqlQuery = "update USERS set USER_ID = ?, EMAIL = ?, LOGIN = ?, USER_NAME = ?," +
                " BIRTHDAY = ? where USER_ID = ?";
        return jdbcTemplate.update(
                sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public Optional<User> loadUserById(long userId) {
        final List<User> users
                = jdbcTemplate.query("select * from USERS where USER_ID = ?", new UserMapper(), userId);

        if (users.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public Collection<User> loadAllUsers() {
        return jdbcTemplate.query("select * from USERS", new UserMapper());
    }
}


