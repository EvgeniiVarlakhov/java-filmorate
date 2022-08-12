package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class UserControllerValidationTest {
    User user;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void createdUser() {
        user = new User(0, "email@", "login",
                "name", LocalDate.of(2010, 1, 1));
    }


    @Test
    public void test1_createdNewUserEmailWithoutAt() throws Exception {
        user.setEmail("email");

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("email не содержит @.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test2_createdNewUserEmailEmpty() throws Exception {
        user.setEmail("");

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("email не может быть пустым.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test3_createdNewUserLoginEmpty() throws Exception {
        user.setLogin("");

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Поле login не может быть пустым.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test4_createdNewUserLoginWithSpace() throws Exception {
        user.setLogin("lo gin");

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Поле login не должно содержать пробелы.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test5_createdNewUserNameEmpty() throws Exception {
        user.setName("");
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gson = gsonBuilder.create();

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk()).andReturn();

        String userJson = mvcResult.getResponse().getContentAsString();
        User newUser = gson.fromJson(userJson, User.class);
        assertEquals("login", newUser.getName());
    }

    @Test
    public void test6_createdNewUserBadBirthday() throws Exception {
        user.setBirthday(LocalDate.of(2025, 12, 1));

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Дата рождения не может быть в будущем.", mvcResult.getResolvedException().getMessage());
    }

}