merge into GENRE (genre_id, genre_name)
values (1,'Комедия');
merge into GENRE (genre_id, genre_name)
    values (2,'Драма');
merge into GENRE (genre_id, genre_name)
    values (3,'Мультфильм');
merge into GENRE (genre_id, genre_name)
    values (4,'Триллер');
merge into GENRE (genre_id, genre_name)
    values (5,'Документальный');
merge into GENRE (genre_id, genre_name)
    values (6,'Боевик');

merge into MPA_RATING (mpa_id, mpa_name, description)
    values (1,'G','У фильма нет возрастных ограничений.');
merge into MPA_RATING (mpa_id, mpa_name, description)
    values (2,'PG','Детям рекомендуется смотреть фильм с родителями.');
merge into MPA_RATING (mpa_id, mpa_name, description)
    values (3,'PG-13','Детям до 13 лет просмотр не желателен.');
merge into MPA_RATING (mpa_id, mpa_name, description)
    values (4,'R','Лицам до 17 лет просматривать фильм можно только в присутствии взрослого.');
merge into MPA_RATING (mpa_id, mpa_name, description)
    values (5,'NC-17','Лицам до 18 лет просмотр запрещён.');