package ru.yandex.practicum.filmorate.exeption;

public class DirectorNotFoundException extends RuntimeException {
    public DirectorNotFoundException(int directorId) {
        super("Режиссер с id" + directorId + " не найден");
    }
    public DirectorNotFoundException(String message) {
        super(message);
    }
}
