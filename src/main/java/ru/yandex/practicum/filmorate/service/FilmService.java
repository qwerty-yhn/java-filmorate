package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;


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

    public Film addLikeToFilm(int id, int userId){
        return filmDbStorage.addLikeToFilm(id, userId);
    }

    public void deleteLikeToFilm(int id, int userId){
        filmDbStorage.deleteLikeToFilm(id,userId);
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
    public Mpa getMpa(int id){
        return filmDbStorage.getMpa(id);
    }
    public List<Mpa> getMpaAll(){
        return filmDbStorage.getMpaAll();
    }
    public Genre getGenres(int id){
        return filmDbStorage.getGenres(id);
    }
    public List<Genre> getGenresAll(){
        return filmDbStorage.getGenresAll();
    }
}
