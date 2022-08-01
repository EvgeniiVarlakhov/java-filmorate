package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test1_createdNewFilm200() throws Exception {
        Film film = new Film("name","desc",LocalDate.of(2020,1,1),100);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }

    @Test
    public void test2_createdNewFilmNameEmpty() throws Exception {
        Film film = new Film("","desc",LocalDate.of(2020,1,1),100);

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Название фильма не может быть пустым.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test3_createdNewFilmBadReleaseDate() throws Exception {
        Film film = new Film("name","desc",LocalDate.of(1800,1,1),100);

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Дата релиза не может быть раньше 28.12.1895."
                , mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test4_createdNewFilmBadDescription() throws Exception {
        Film film = new Film("name","descdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
                ,LocalDate.of(2020,1,1),100);

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Длина описания не может быть более 200 символов."
                , mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test5_createdNewFilm1BadDuration() throws Exception {
        Film film = new Film("name","desc",LocalDate.of(2020,1,1),-100);

        MvcResult mvcResult = mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Продолжительность фильма не может быть отрицательной."
                , mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test6_updateFilm() throws Exception {
        Film film = new Film("name","desc",LocalDate.of(2020,1,1),100);
        Film film1 = new Film("newName","desc",LocalDate.of(2020,1,1),100);
        film1.setId(1);
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gson = gsonBuilder.create();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(film1)))
                .andExpect(status().isOk()).andReturn();

        String filmJson = mvcResult.getResponse().getContentAsString();
        Film updateFilm = gson.fromJson(filmJson, Film.class);
        assertEquals("newName", updateFilm.getName());
    }

    @Test
    public void test7_getListOfFilms() throws Exception {
        mockMvc.perform(get("/films")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
