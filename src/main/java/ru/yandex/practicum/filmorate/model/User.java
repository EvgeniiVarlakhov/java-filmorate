package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    private long id;
    private String email;
    private String login;
    private String name = "";
    private LocalDate birthday;

}
