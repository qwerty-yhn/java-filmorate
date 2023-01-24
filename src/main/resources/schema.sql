DROP ALL OBJECTS;

create table IF NOT EXISTS films
(
    id          INTEGER generated by default as identity primary key,
    name        varchar(255) not null,
    description varchar(255),
    releaseDate date         not null,
    duration    INTEGER          not null

);

create table IF NOT EXISTS genre
(
    id   INTEGER      not null primary key,
    name varchar(255) not null
);

create table IF NOT EXISTS directors
(
    id   INTEGER primary key auto_increment,
    name varchar(255) not null
);

create table IF NOT EXISTS film_directors
(
    film_id integer not null,
    director_id integer not null,
    foreign key (film_id) references films (id) on delete cascade,
    foreign key (director_id) references directors (id) on delete cascade
);

create table IF NOT EXISTS mpa
(
    id   INTEGER generated by default as identity primary key,
    name varchar(255) not null
);

create table IF NOT EXISTS mpa_films
(
    id_film INTEGER not null references films (id),
    id_mpa  INTEGER not null references mpa (id)
);

create table IF NOT EXISTS filmid_genreid
(
    id_film  INTEGER not null references films (id),
    id_genre INTEGER not null references genre (id)
);



create table IF NOT EXISTS users
(
    id       INTEGER generated by default as identity primary key,
    email    varchar(255),
    login    varchar(255),
    name     varchar(255),
    birthday date
);

create table IF NOT EXISTS userid_filmid
(
    id      INTEGER primary key,
    id_film INTEGER not null references films (id),
    id_user INTEGER not null references users (id)
);

create table IF NOT EXISTS friendship
(
    id        INTEGER generated by default as identity primary key,
    id_user   INTEGER not null references users (id),
    id_friend INTEGER not null,
    confirm   boolean
);

create table IF NOT EXISTS like_film
(
    film_id INTEGER not null references films (id) ON DELETE CASCADE,
    user_id INTEGER not null references users (id) ON DELETE CASCADE
);