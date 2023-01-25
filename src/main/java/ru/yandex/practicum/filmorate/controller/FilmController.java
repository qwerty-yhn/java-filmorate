package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

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

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @DeleteMapping("/films/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {
        log.info("method = 'DELETE' endpoint = '/films/{filmId}' id = '" + filmId + "'");
        filmService.removeFilm(filmId);
    }

}
