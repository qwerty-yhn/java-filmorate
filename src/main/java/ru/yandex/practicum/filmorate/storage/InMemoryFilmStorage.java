package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Calendar.DECEMBER;
@Component
public class InMemoryFilmStorage implements FilmStorage{

    private Long generatorId = 0L;

    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    private HashMap<Long, Film> filmHashMap = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        log.info("Получен запрос POST/films");

        assertFilm(film);

        film.setId(incIdFilm().intValue());
        filmHashMap.put(Long.valueOf(film.getId()), film);
        return filmHashMap.get(Long.valueOf(film.getId()));
    }

    @Override
    public void deleteFilm(Film film) {

    }

    public Film getFilmId(Long id){
        if(filmHashMap.containsKey(id)){
            return filmHashMap.get(id);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Actor Not Found");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Получен запрос POST/films");

        assertFilm(film);

        if(filmHashMap.containsKey(Long.valueOf(film.getId()))){
            int id = filmHashMap.get(Long.valueOf(film.getId())).getId();
            film.setId(id);
            filmHashMap.put(Long.valueOf(film.getId()), film);
            return filmHashMap.get(Long.valueOf(film.getId()));
        }
        else{
            log.error("Нет такого film");
            throw new VaidationExeption("Нет такого film");
        }
    }

    public List<Film> getFilms(){

        List<Film> filmsList = new ArrayList<>();
        log.info("Получен запрос /users");
        for(Long inc: filmHashMap.keySet()){
            filmsList.add(filmHashMap.get(inc));
        }
        return filmsList;
    }

    private Long incIdFilm(){
        generatorId = generatorId + 1;
        return generatorId;
    }
    @Validated
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
