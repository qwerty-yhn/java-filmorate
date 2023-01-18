package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    public Genre getGenres(int id);
    public List<Genre> getGenresAll();
}
