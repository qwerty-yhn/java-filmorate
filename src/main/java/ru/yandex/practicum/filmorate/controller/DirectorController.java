package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
@Slf4j
public class DirectorController {
    @Autowired
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAll(){
        List<Director> directors = directorService.getAll();
        log.info("Возвращен список всех режиссеров {}", directors);
        return directors;
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable int id) {
        Director director = directorService.getById(id);
        log.info("Возвращен режиссер {}", director);
        return director;
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        Director newDirector = directorService.create(director);
        log.info("Добавлен режиссер {}", newDirector);
        return newDirector;
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        Director updatedDirector = directorService.update(director);
        log.info("Обновлен режиссер {}", updatedDirector);
        return updatedDirector;
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable int id) {
        directorService.remove(id);
        log.info("Удален режиссер с id={}", id);
    }
}

