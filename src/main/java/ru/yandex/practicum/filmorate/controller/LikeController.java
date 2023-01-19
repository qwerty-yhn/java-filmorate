package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.LikeService;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class LikeController {
    @Autowired
    private LikeService likeService;
    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("methot = 'DELETE' endpoint = '/films/{id}/like/{userId'");
        likeService.deleteLikeToFilm(id, userId);
    }
    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("methot = 'PUT' endpoint = '/films/{id}/like/{userId}'");
        return likeService.addLikeToFilm(id, userId);
    }

}
