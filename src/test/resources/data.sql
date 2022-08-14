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

merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values ( 10,'email10','login10','name10','2022-01-01');
merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values ( 20,'email20','login20','name20','2022-01-01');
merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values ( 30,'email30','login30','name30','2022-01-01');
merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values ( 40,'email40','login40','name40','2022-01-01');
merge into USERS (USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY) values ( 50,'email50','login50','name50','2022-01-01');

merge into FILMS (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING )
    values ( 10,'name10', 'des10', '2022-01-01',10,1);
merge into FILMS (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING )
    values ( 20,'name10', 'des10', '2022-01-01',10,1);

merge into FRIENDSHIP (USER_ID, FRIEND_ID) values ( 20,10);

merge into FILMS_GENRE (FILM_ID, GENRE_ID) values ( 20,1);

merge into USERS_FILMS (FILM_ID, USER_ID) values ( 20,10);
merge into USERS_FILMS (FILM_ID, USER_ID) values ( 20,20);
merge into USERS_FILMS (FILM_ID, USER_ID) values ( 20,30);