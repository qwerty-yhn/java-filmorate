package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
@Service
@RequiredArgsConstructor
public class MpaService {
    @Autowired
    private final MpaDbStorage mpaDbStorage;
    public Mpa getMpa(int id){
        return mpaDbStorage.getMpa(id);
    }
    public List<Mpa> getMpaAll(){
        return mpaDbStorage.getMpaAll();
    }
}
