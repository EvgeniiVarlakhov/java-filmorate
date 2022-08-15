package ru.yandex.practicum.filmorate.storage.daoStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.mapper.UserMapper;

import java.util.Collection;

@Component
public class FriendDbStorage implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getListOfFriends(long idUser) {
        String sqlQuery = "select u.* from USERS u join FRIENDSHIP f ON u.USER_ID = f.FRIEND_ID where f.USER_ID =?";

        return jdbcTemplate.query(sqlQuery, new UserMapper(), idUser);
    }

    @Override
    public void addFriend(long idUser, long idFriend) {
        String sqlQuery = "insert into FRIENDSHIP ( USER_ID, FRIEND_ID ) values (?,?)";

        jdbcTemplate.update(sqlQuery, idUser, idFriend);
    }

    @Override
    public void deleteFriend(long idUser, long idFriend) {
        String sqlQuery = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ? ";

        jdbcTemplate.update(sqlQuery, idUser, idFriend);
    }

    @Override
    public Collection<User> getSameFriend(long idUser, long idOtherUser) {
        String sqlQuery = "select u.* " +
                "from USERS u " +
                "join FRIENDSHIP f ON u.USER_ID = f.FRIEND_ID " +
                "where f.USER_ID =? and f.FRIEND_ID != ? " +
                "union " +
                "select u.* " +
                "from USERS u " +
                "join FRIENDSHIP f ON u.USER_ID = f.FRIEND_ID " +
                "where f.USER_ID =? and f.FRIEND_ID != ?";

        return jdbcTemplate.query(sqlQuery, new UserMapper(), idUser, idOtherUser, idOtherUser, idUser);
    }

}
