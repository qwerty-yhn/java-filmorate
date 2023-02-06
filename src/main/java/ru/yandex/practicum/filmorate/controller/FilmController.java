package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

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

    @GetMapping("/films/director/{directorId}")
    public List<Film> getTopFilmsDirector(@PathVariable int directorId, @RequestParam String sortBy) {
        List<Film> topFilmsDirector = filmService.getTopFilmsDirector(directorId, sortBy);
        if (sortBy.equals("year")) {
            log.info("Возвращен список режиссеров отсортированный по годам {}", topFilmsDirector);
        } else if (sortBy.equals("likes")) {
            log.info("Возвращен список режиссеров отсортированный по лафкам {}", topFilmsDirector);
        } else {
            log.info("Неверный тип сортировки");
        }
        return topFilmsDirector;
    }

    @DeleteMapping("/films/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {
        log.info("method = 'DELETE' endpoint = '/films/{filmId}' id = '" + filmId + "'");
        filmService.removeFilm(filmId);
    }
    @GetMapping("/users/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable int id) {


        return filmService.getRecommendations(id);
    }

    @GetMapping("/films/search")
    public List<Film> searchFilm(@RequestParam String query,
                                 @RequestParam String by) {
        log.info("method = 'GET' endpoint = '/films/search' query = '{}' by = '{}'", query, by);
        return filmService.searchFilm(query, by);
    }


    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        List<Film> popularFilms = filmService.getPopularFilms(count, genreId, year);
        log.info("Возвращен отсортированный список фильмов {}", popularFilms);
        return popularFilms;
    }
    
    @GetMapping("/films/common")
    public List<Film> getCommonFilms(@RequestParam Integer userId, @RequestParam Integer friendId) {
        log.info("method = 'GET' endpoint = '/films/common' userId = '{}' friendId = '{}'", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);

    }
}
