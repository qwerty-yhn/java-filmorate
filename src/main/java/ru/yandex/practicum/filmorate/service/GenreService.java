package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    private final GenreDbStorage genreDbStorage;
    public Genre getGenres(int id){
        return genreDbStorage.getGenres(id);
    }
    public List<Genre> getGenresAll(){
        return genreDbStorage.getGenresAll();
    }
}
