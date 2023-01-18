package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

interface FilmStorage {

    Film addFilm(Film film);

    void deleteFilm(Film film);

    Film updateFilm(Film film);


}
