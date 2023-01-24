package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.*;


@Service
@RequiredArgsConstructor
public class FilmService {

    @Autowired
    private final FilmDbStorage filmDbStorage;
    private final DirectorStorage directorStorage;

    public Film createFilm(Film film){
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm(Film film){
        return filmDbStorage.updateFilm(film);
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

    public List<Film> getTopFilmsDirector(int directorId, String sorting) {
        checkingPresenceDirector(directorId);
        return filmDbStorage.getTopFilmsDirector(directorId, sorting);
    }

    private void checkingPresenceDirector(Integer directorId) { // Проверка наличия режиссера в хранилище
        if (directorStorage.getById(directorId) != null) {
            return;
        }
        throw new DirectorNotFoundException("Режиссер с id" + directorId + " не найден");
    }
}
