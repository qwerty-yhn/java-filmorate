package ru.yandex.practicum.filmorate.exeption;

public class FilmNotFoundExeption extends RuntimeException {
    public FilmNotFoundExeption(String message) {
        super(message);
    }
}