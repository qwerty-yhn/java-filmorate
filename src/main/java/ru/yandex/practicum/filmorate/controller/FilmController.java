package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Calendar.DECEMBER;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class FilmController {

    private int generatorId = 0;
    private HashMap<Integer, Film> filmHashMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film){
        log.info("Получен запрос POST/films");

        assertFilm(film);

        film.setId(incId());
        filmHashMap.put(film.getId(), film);
        return filmHashMap.get(film.getId());
    }
    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film){
        log.info("Получен запрос POST/films");

        assertFilm(film);

        if(filmHashMap.containsKey(film.getId())){
            int id = filmHashMap.get(film.getId()).getId();
            film.setId(id);
            filmHashMap.put(film.getId(), film);
            return filmHashMap.get(film.getId());
        }
        else{
            log.error("Нет такого film");
            throw new VaidationExeption("Нет такого film");
        }
    }
    @GetMapping("/films")
    public List<Film> getFilms(){
        List<Film> filmsList = new ArrayList<>();
        log.info("Получен запрос /users");
        for(Integer in: filmHashMap.keySet()){
            filmsList.add(filmHashMap.get(in));
        }
        return filmsList;
    }
    private int incId(){
        generatorId = generatorId + 1;
        return generatorId;}
    private void assertFilm(Film film){
        if(film.getName() == ""){
            log.error("Название не может быть пустым");
            throw new VaidationExeption("Название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описание - 200 символов");
            throw new VaidationExeption("Максимальная длина описание - 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            log.error("Дата релиза - не раньше 28 декабря 1895 года");
            throw new VaidationExeption("Дата релиза - не раньше 28 декабря 1895 года");
        }else if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не должна быть положительной");
            throw new VaidationExeption("Продолжительность фильма не должна быть положительной");
        }
    }
}
