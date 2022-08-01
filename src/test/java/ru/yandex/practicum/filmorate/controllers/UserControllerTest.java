package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void test1_createdNewUser200() throws Exception {
        User user = new User("1212@12", "login", LocalDate.of(2020, 12, 1));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void test2_createdNewUserEmailWithoutAt() throws Exception {
        User user = new User("121212", "login", LocalDate.of(2020, 12, 1));

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("email не содержит @.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test3_createdNewUserEmailEmpty() throws Exception {
        User user = new User("", "login", LocalDate.of(2020, 12, 1));

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("email не может быть пустым.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test4_createdNewUserLoginEmpty() throws Exception {
        User user = new User("1212@12", "", LocalDate.of(2020, 12, 1));

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Поле login не может быть пустым.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test5_createdNewUserLoginWithSpace() throws Exception {
        User user = new User("1212@12", "lo gin", LocalDate.of(2020, 12, 1));

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Поле login не должно содержать пробелы.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test6_createdNewUserNameEmpty() throws Exception {
        User user = new User("1212@12", "login", LocalDate.of(2020, 12, 1));
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
    public void test7_createdNewUserBadBirthday() throws Exception {
        User user = new User("1212@12", "login", LocalDate.of(2025, 12, 1));

        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Дата рождения не может быть в будущем.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test8_updateUser() throws Exception {
        User user = new User("1212@12", "login", LocalDate.of(2020, 12, 1));
        User newUser = new User("1111@11", "login", LocalDate.of(2020, 12, 1));
        newUser.setId(1);
        Gson gson;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gson = gsonBuilder.create();

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk()).andReturn();

        String userJson = mvcResult.getResponse().getContentAsString();
        User updateUser = gson.fromJson(userJson, User.class);
        assertEquals("1111@11", updateUser.getEmail());
    }

    @Test
    public void test9_updateUserDontExist() throws Exception {
        User user = new User("1212@12", "login", LocalDate.of(2020, 12, 1));
        User newUser = new User("1111@11", "login", LocalDate.of(2020, 12, 1));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isNotFound()).andReturn();

        assertEquals("Пользователь с таким ID не существует.", mvcResult.getResolvedException().getMessage());
    }

    @Test
    public void test10_getListOfUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}