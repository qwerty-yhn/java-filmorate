package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    private final InMemoryFilmStorage inMemoryFilmStorage;

    public Film createFilm(Film film){
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film addLikeToFilm(Long id, Long userId){
        Film film = inMemoryFilmStorage.getFilmId(id);
        film.getLikes().add(userId);
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    public void deleteLikeToFilm(Long id, Long userId){
        if(inMemoryFilmStorage.getFilmId(id).getLikes().contains(userId)){
            Film film = inMemoryFilmStorage.getFilmId(id);
            film.getLikes().remove(userId);
            inMemoryFilmStorage.updateFilm(film);
        }
        else{
            throw new FilmNotFoundExeption("нет такого фильма");
        }
    }

    public List<Film> getTopFilms(int count) {
        return inMemoryFilmStorage.getFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmId(Long id){
        return inMemoryFilmStorage.getFilmId(id);
    }

    public List<Film> getFilms(){
        return inMemoryFilmStorage.getFilms();
    }
}
