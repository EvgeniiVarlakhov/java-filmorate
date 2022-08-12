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
    public void testUserDbStorage() {

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

        userOptional = userStorage.loadUserById(1);

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

        userStorage.overwriteUser(
                new User(1, "email1", "login1", "name2",
                        LocalDate.of(2022, 1, 1)));

        userOptional = userStorage.loadUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "name2")
                );

        userStorage.deleteUser(1);

        userOptional = userStorage.loadUserById(1);
        assertThat(userOptional).isEmpty();

        Collection<User> users = userStorage.loadAllUsers();
        assertThat(users.size()).isEqualTo(3);

    }

    @Test
    public void testFriendDbStorage() {
        List<User> friendsTest = new ArrayList<>(friendDao.getListOfFriends(10));

        assertThat(friendsTest.size()).isEqualTo(0);

        friendDao.addFriend(10, 20);
        friendDao.addFriend(30, 20);

        friendsTest = new ArrayList<>(friendDao.getListOfFriends(10L));

        assertThat(friendsTest.size()).isEqualTo(1);
        assertThat(friendsTest.get(0).getId()).isEqualTo(20L);

        friendsTest = new ArrayList<>(friendDao.getSameFriend(10, 30));

        assertThat(friendsTest.size()).isEqualTo(1);
        assertThat(friendsTest.get(0).getId()).isEqualTo(20L);

        friendDao.deleteFriend(10, 20);

        friendsTest = new ArrayList<>(friendDao.getListOfFriends(10L));

        assertThat(friendsTest.size()).isEqualTo(0);

    }

    @Test
    public void testGenreDbStorage() {

        Optional<Genre> genreOptional = genreDao.loadGenreById(2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма")
                );

        List<Genre> genres = new ArrayList<>(genreDao.loadAllGenre());

        assertThat(genres.size()).isEqualTo(6);

        Film film = new Film();
        Genre genre = new Genre();
        HashSet<Genre> genresSet = new HashSet<>();
        genre.setId(5);
        film.setId(10);
        genresSet.add(genre);
        film.setGenres(genresSet);

        genreDao.saveFilmGenre(film);

        genres = new ArrayList<>(genreDao.loadGenreIntoFilm(10));

        assertThat(genres.size()).isEqualTo(1);
        assertThat(genres.get(0).getName()).isEqualTo("Документальный");

        genreDao.deleteFilmGenre(10);

        genres = new ArrayList<>(genreDao.loadGenreIntoFilm(10));

        assertThat(genres.size()).isEqualTo(0);
    }

    @Test
    public void testLikeDbStorage() {

        likeDao.saveLikeToFilm(10, 10);
        likeDao.saveLikeToFilm(20, 10);
        likeDao.saveLikeToFilm(20, 20);

        List<Film> filmsList = new ArrayList<>(likeDao.loadTopOfFilm(2));

        assertThat(filmsList.size()).isEqualTo(2);
        assertThat(filmsList.get(0).getId()).isEqualTo(20L);
        assertThat(filmsList.get(1).getId()).isEqualTo(10L);

        likeDao.deleteLikeFromFilm(20, 10);
        likeDao.deleteLikeFromFilm(20, 20);

        filmsList = new ArrayList<>(likeDao.loadTopOfFilm(2));

        assertThat(filmsList.size()).isEqualTo(2);
        assertThat(filmsList.get(0).getId()).isEqualTo(10L);
        assertThat(filmsList.get(1).getId()).isEqualTo(20L);

    }

    @Test
    public void testMpaRatingDbStorage() {
        List<MpaRating> mpaList = new ArrayList<>(mpaRatingDao.loadAllMpaRating());

        assertThat(mpaList.size()).isEqualTo(5);

        Optional<MpaRating> mpaObject = mpaRatingDao.loadMpaRatingById(2);

        assertThat(mpaObject)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    public void testFilmDbStorage() {
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

        filmOptional = filmStorage.getFilmById(1);

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

        List<Film> filmsList = new ArrayList<>(filmStorage.getFilmsList());

        assertThat(filmsList.size()).isEqualTo(3);
        assertThat(filmsList.get(0).getId()).isEqualTo(1);

        filmOptional.get().setName("updateName");

        filmStorage.overwriteFilm(filmOptional.get());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updateName")
                );

        filmStorage.removeFilm(1);

        filmOptional = filmStorage.getFilmById(1);

        assertThat(filmOptional).isEmpty();
    }

}
