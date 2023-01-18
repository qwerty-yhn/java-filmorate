package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class GenreController {
    @Autowired
    private GenreService genreService;
    @GetMapping("/genres/{id}")
    public Genre getGenres(@PathVariable int id) {
        return genreService.getGenres(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenresAll() {
        return genreService.getGenresAll();
    }
}
