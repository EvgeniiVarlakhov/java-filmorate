package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.dao.FriendDao;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final MpaRatingDao mpaRatingDao;
    private final GenreDao genreDao;
    private final FriendDao friendDao;
    private final LikeDao likeDao;
    private final FilmStorage filmStorage;

    @Test
    public void testUserGetAllUsers() {
        List<User> usersList = new ArrayList<>(userStorage.loadAllUsers());

        assertThat(usersList.size()).isEqualTo(5);
    }

    @Test
    public void testUserDbStorageAddUserDeleteUser() {

        Optional<User> userOptional = Optional.of(
                userStorage.addUser(
                        new User(0, "email1", "login1", "name1",
                                LocalDate.of(2022, 1, 1))));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "email1")
                );
        userStorage.deleteUser(1);

        userOptional = userStorage.loadUserById(1);
        assertThat(userOptional).isEmpty();

        Collection<User> users = userStorage.loadAllUsers();
        assertThat(users.size()).isEqualTo(5);
    }

    @Test
    public void testUserDbStorageGetUserByID() {
        Optional<User> userOptional = userStorage.loadUserById(10);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 10L)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "email10")
                );
    }

    @Test
    public void testUserDbStorageOverwriteUser() {
        userStorage.overwriteUser(
                new User(10, "email10", "login10", "nameTest",
                        LocalDate.of(2022, 1, 1)));

        Optional<User> userOptional = userStorage.loadUserById(10);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 10L)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "nameTest")
                );
    }

    @Test
    public void testFriendDbStorageGetListOfFriends() {
        List<User> friendsTest = new ArrayList<>(friendDao.getListOfFriends(10));

        assertThat(friendsTest.size()).isEqualTo(0);
    }

    @Test
    public void testFriendDbStorageAddFriend() {

        friendDao.addFriend(50, 10);

        List<User> friendsTest = new ArrayList<>(friendDao.getListOfFriends(50L));

        assertThat(friendsTest.size()).isEqualTo(1);
        assertThat(friendsTest.get(0).getId()).isEqualTo(10L);
    }

    @Test
    public void testFriendDbStorageGetSameFriend() {

        friendDao.addFriend(10, 40);
        friendDao.addFriend(30, 40);

        List<User> friendsTest = new ArrayList<>(friendDao.getSameFriend(10, 30));
        System.out.println(friendsTest);
        assertThat(friendsTest.size()).isEqualTo(1);
        assertThat(friendsTest.get(0).getId()).isEqualTo(40L);
    }

    @Test
    public void testFriendDbStorageDeleteFriend() {

        friendDao.deleteFriend(20, 10);

        List<User> friendsTest = new ArrayList<>(friendDao.getListOfFriends(20L));

        assertThat(friendsTest.size()).isEqualTo(0);
    }

    @Test
    public void testGenreDbStorageLoadGenreById() {

        Optional<Genre> genreOptional = genreDao.loadGenreById(2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    public void testGenreDbStorageLoadAllGenre() {

        List<Genre> genres = new ArrayList<>(genreDao.loadAllGenre());

        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    public void testGenreDbStorageSaveFilmGenre() {
        Film film = new Film();
        Genre genre = new Genre();
        HashSet<Genre> genresSet = new HashSet<>();
        genre.setId(5);
        film.setId(10);
        genresSet.add(genre);
        film.setGenres(genresSet);

        genreDao.saveFilmGenre(film);

        List<Genre> genres = new ArrayList<>(genreDao.loadGenreIntoFilm(10));

        assertThat(genres.size()).isEqualTo(1);
        assertThat(genres.get(0).getName()).isEqualTo("Документальный");
    }

    @Test
    public void testGenreDbStorageLoadGenreIntoFilm() {
        List<Genre> genres = new ArrayList<>(genreDao.loadGenreIntoFilm(20));

        assertThat(genres.size()).isEqualTo(1);
        assertThat(genres.get(0).getName()).isEqualTo("Комедия");
    }

    @Test
    public void testGenreDbStorageDeleteFilmGenre() {
        genreDao.deleteFilmGenre(20);

        List<Genre> genres = new ArrayList<>(genreDao.loadGenreIntoFilm(20));

        assertThat(genres.size()).isEqualTo(0);
    }

    @Test
    public void testLikeDbStorageSaveLikeToFilm() {
        int status = likeDao.saveLikeToFilm(10, 10);

        assertThat(status).isNotZero();
    }

    @Test
    public void testLikeDbStorageLoadTopOfFilm() {
        List<Film> filmsList = new ArrayList<>(likeDao.loadTopOfFilm(2));

        assertThat(filmsList.size()).isEqualTo(2);
        assertThat(filmsList.get(0).getId()).isEqualTo(20L);
        assertThat(filmsList.get(1).getId()).isEqualTo(10L);
    }

    @Test
    public void testLikeDbStorageDeleteLikeOfFilm() {
        int status = likeDao.deleteLikeFromFilm(20, 10);

        assertThat(status).isNotZero();

        List<Film> filmsList = new ArrayList<>(likeDao.loadTopOfFilm(2));

        assertThat(filmsList.size()).isEqualTo(2);
        assertThat(filmsList.get(0).getId()).isEqualTo(20L);
        assertThat(filmsList.get(1).getId()).isEqualTo(10L);
    }

    @Test
    public void testMpaRatingDbStorageLoadAllMpaRating() {
        List<MpaRating> mpaList = new ArrayList<>(mpaRatingDao.loadAllMpaRating());

        assertThat(mpaList.size()).isEqualTo(5);
    }

    @Test
    public void testMpaRatingDbStorageLoadMpaRatingById() {
        Optional<MpaRating> mpaObject = mpaRatingDao.loadMpaRatingById(2);

        assertThat(mpaObject)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    public void testFilmDbStorageAddFilmRemoveFilm() {
        Genre genreFilm = new Genre();
        genreFilm.setId(1);
        Set<Genre> genreSet = new HashSet<>();
        genreSet.add(genreFilm);
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(1);
        Film newFilm = new Film(
                0, "film1", "des1", LocalDate.of(2020, 1, 1),
                100, mpaRating, genreSet);

        Optional<Film> filmOptional = Optional.of(filmStorage.addFilm(newFilm));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "film1")
                );
        filmStorage.removeFilm(1);
        filmOptional = filmStorage.getFilmById(1);

        assertThat(filmOptional).isEmpty();
    }

    @Test
    public void testFilmDbStorageGetFilmById() {
        Optional<Film> filmOptional = filmStorage.getFilmById(10);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 10L)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "name10")
                );
    }

    @Test
    public void testFilmDbStorageGetFilmsList() {

        List<Film> filmsList = new ArrayList<>(filmStorage.getFilmsList());

        assertThat(filmsList.size()).isEqualTo(2);
        assertThat(filmsList.get(0).getId()).isEqualTo(10L);
    }

    @Test
    public void testFilmDbStorageOverwriteFilm() {
        Optional<Film> filmOptional = filmStorage.getFilmById(20);
        filmOptional.get().setName("updateName");

        filmStorage.overwriteFilm(filmOptional.get());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updateName")
                );
    }

}
