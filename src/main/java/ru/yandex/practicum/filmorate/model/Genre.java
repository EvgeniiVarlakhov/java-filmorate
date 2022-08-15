package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Genre {
    private long id;
    private String name;
}

