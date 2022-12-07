
package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

import ru.yandex.practicum.filmorate.exeption.VaidationExeption;

public class FilmControllerTest {

    @Test
    public void testAddFilm(){

        Film film = new Film();
        film.setId(1);
        film.setName("Приключение Шурика");
        film.setDescribtion("Совесткая комедия");
        film.setReleaseDate(LocalDate.of(1965, Month.JULY, 23));
        film.setDuration(Duration.ofMinutes(95));

        FilmController filmController = new FilmController();

        filmController.addFilm(film);

        Assertions.assertEquals(film, filmController.addFilm(film));

        film.setId(1);
        film.setName("");
        film.setDescribtion("Совесткая комедия");
        film.setReleaseDate(LocalDate.of(1965, Month.JULY, 23));
        film.setDuration(Duration.ofMinutes(95));

        try {
            filmController.addFilm(film);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        film.setId(1);
        film.setName("Приключение Шурика");
        film.setDescribtion("...........................................................................................................................................................................................................");
        film.setReleaseDate(LocalDate.of(1965, Month.JULY, 23));
        film.setDuration(Duration.ofMinutes(95));

        try {
            filmController.addFilm(film);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        film.setId(1);
        film.setName("Приключение Шурика");
        film.setDescribtion("Совесткая комедия");
        film.setReleaseDate(LocalDate.of(1894, Month.DECEMBER, 28));
        film.setDuration(Duration.ofMinutes(95));

        try {
            filmController.addFilm(film);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        film.setId(1);
        film.setName("Приключение Шурика");
        film.setDescribtion("Совесткая комедия");
        film.setReleaseDate(LocalDate.of(1965, Month.JULY, 23));
        film.setDuration(Duration.ofMinutes(-95));

        try {
            filmController.addFilm(film);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }
    }
}