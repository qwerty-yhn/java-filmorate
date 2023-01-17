package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.util.Calendar.DECEMBER;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmController {

    @Autowired
    private FilmService filmService;

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("methot = 'PUT' endpoint = '/films' id = '" + film.getName() + "'");
        return filmService.updateFilm(film);
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("methot = 'POST' endpoint = '/films' id = '" + film.getName() + "'");
        return filmService.createFilm(film);
    }

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        log.info("methot = 'GET' endpoint = '/films'");
        Collection<Film> tst = filmService.getFilms();
        return tst;
    }

    @GetMapping("/films/{id}")
    public Film getFilmId(@PathVariable int id) {
        log.info("methot = 'GET' endpoint = '/films/{id}'");
        return filmService.getFilmId(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("methot = 'PUT' endpoint = '/films/{id}/like/{userId}'");
        return filmService.addLikeToFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("methot = 'DELETE' endpoint = '/films/{id}/like/{userId'");
        filmService.deleteLikeToFilm(id, userId);
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return filmService.getMpa(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpaAll() {
        return filmService.getMpaAll();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenres(@PathVariable int id) {
        return filmService.getGenres(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenresAll() {
        return filmService.getGenresAll();
    }
}
