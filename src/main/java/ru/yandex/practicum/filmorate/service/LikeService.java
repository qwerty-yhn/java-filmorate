package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

@Service
@RequiredArgsConstructor
public class LikeService {
    @Autowired
    private final LikeDbStorage likeDbStorage;
    public Film addLikeToFilm(int id, int userId){
        return likeDbStorage.addLikeToFilm(id, userId);
    }

    public void deleteLikeToFilm(int id, int userId){
        likeDbStorage.deleteLikeToFilm(id,userId);
    }
}
