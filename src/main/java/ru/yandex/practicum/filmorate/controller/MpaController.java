package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class MpaController {
    @Autowired
    private MpaService mpaService;

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return mpaService.getMpa(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpaAll() {
        return mpaService.getMpaAll();
    }
}
