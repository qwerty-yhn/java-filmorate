package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.*;


@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    private final FilmDbStorage filmDbStorage;

    public Film createFilm(Film film){
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return filmDbStorage.updateFilm(film);
    }

    public void removeFilm(int id) {
        filmDbStorage.deleteFilm(filmDbStorage.getFilmId(id));
    }

    public List<Film> getTopFilms(int count) {
        return filmDbStorage.getTopFilms(count);
    }

    public Film getFilmId(int id){
        return filmDbStorage.getFilmId(id);
    }

    public Collection<Film> getFilms(){
        return filmDbStorage.getFilms();
    }

    public List<Film> getRecommendations(int id){return filmDbStorage.getRecommendations(id);}

}
