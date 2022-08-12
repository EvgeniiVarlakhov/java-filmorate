package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendDao {

    Collection<User> getListOfFriends(long idUser);

    void addFriend(long idUser, long idFriend);

    void deleteFriend(long idUser, long idFriend);

    Collection<User> getSameFriend(long idUser, long idOtherUser);

}
