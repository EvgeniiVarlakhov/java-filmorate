package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class FilmControllerValidationTest {
    Film film = new Film();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void createdFilm() {
        HashSet<Genre> genres = new HashSet<>();
        genres.add(new Genre());
        film = new Film(0, "name10", "des10", LocalDate.of(2000, 1, 1),
                10, new MpaRating(), genres);
    }

    @Test
    public void test1_createdNewFilmNameEmpty() throws Exception {
        film.setName("");
        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Название фильма не может быть пустым.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test2_createdNewFilmBadReleaseDate() throws Exception {
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Дата релиза не может быть раньше 28.12.1895."
                , mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test3_createdNewFilmBadDescription() throws Exception {
        film.setDescription("descdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Длина описания не может быть более 200 символов."
                , mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test4_createdNewFilm1BadDuration() throws Exception {
        film.setDuration(-100);

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Продолжительность фильма не может быть отрицательной."
                , mvcResult.getResolvedException().getMessage());
    }

}
