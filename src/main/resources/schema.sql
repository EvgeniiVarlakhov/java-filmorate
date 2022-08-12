create table if not exists USERS(
    user_id long primary key auto_increment,
    email varchar (300) not null unique,
    login varchar (300) not null unique,
    user_name varchar (300) not null,
    birthday date
);

create table if not exists MPA_RATING (
    mpa_id long primary key ,
    mpa_name varchar (300),
    description varchar (500)
);

create table if not exists FILMS
(
    film_id long primary key auto_increment,
    film_name varchar (300) not null,
    description varchar (3000) not null,
    release_date date not null,
    duration int not null,
    mpa_rating long not null,
    constraint fk_mpa_rating foreign key (mpa_rating ) references MPA_RATING (mpa_id)
);

create table if not exists FRIENDSHIP (
    user_id long references USERS (user_id) on delete cascade,
    friend_id long references USERS (user_id) on delete cascade,
    constraint pk_friendship primary key (user_id,friend_id)
);

create table if not exists GENRE (
    genre_id long primary key ,
    genre_name varchar (300) not null
);

create table if not exists FILMS_GENRE (
    film_id long references FILMS (film_id),
    genre_id long references GENRE (genre_id),
    constraint pk_films_genre primary key (film_id,genre_id)
);

create table if not exists USERS_FILMS(
    film_id long references FILMS(film_id),
    user_id long references USERS(user_id),
   constraint pk_users_films primary key (film_id,user_id)
);