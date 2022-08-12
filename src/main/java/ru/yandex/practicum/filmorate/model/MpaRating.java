package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")

public class MpaRating {
    private long id;
    private String name;
    private String description;
}
