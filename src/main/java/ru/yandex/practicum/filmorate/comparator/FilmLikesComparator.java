package ru.yandex.practicum.filmorate.comparator;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmLikesComparator implements Comparator<Film> {

    @Override
    public int compare(Film film1, Film film2) {

        return 1; //Integer.compare(film2.getLikes().size(), film1.getLikes().size());
    }

}
