package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikeStorage {

    public Film addLikeToFilm(int id, int userId);
    public void deleteLikeToFilm(int id, int userId);

}
