package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@Slf4j
public class DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<Director> getAll(){
        return directorStorage.getAll();
    }

    public Director getById(int id) {
        return directorStorage.getById(id);
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        checkingPresenceDirector(director.getId());
        return directorStorage.update(director);
    }

    public void remove(int id) {
        checkingPresenceDirector(id);
        directorStorage.remove(id);
    }

    private void checkingPresenceDirector(Integer directorId) { // Проверка наличия режиссера в хранилище
        if (directorStorage.getById(directorId) != null) {
            return;
        }
        throw new DirectorNotFoundException("Режиссер с id" + directorId + " не найден");
    }
}
