package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/mpa")
    public Collection<MpaRating> getAllMpaRating() {
        return filmService.getListOfMpaRating();
    }

    @GetMapping("/mpa/{id}")
    public MpaRating getMpaRatingById(@PathVariable Long id) {
        return filmService.getMpaRatingById(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilmList(
            @RequestParam(value = "count", defaultValue = "10", required = false) Long count) {
        return filmService.getTopFilm(count);
    }

    @GetMapping("/genres")
    public Collection<Genre> getAllGenre() {
        return filmService.getListOfGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return filmService.getGenreById(id);
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLikeToFilm(id, userId);
    }

}
