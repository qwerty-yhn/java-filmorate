package ru.yandex.practicum.filmorate.exeption;

public class VaidationExeption extends RuntimeException {
    public VaidationExeption(String message) {
        super(message);
    }
}