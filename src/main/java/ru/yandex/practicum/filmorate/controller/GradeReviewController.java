package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GradeReviewService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GradeReviewController {
    private final GradeReviewService gradeReviewService;

    @PutMapping("/reviews/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Добавление лайка пользователем {} отзыву {}", userId, id);
        gradeReviewService.addLike(id, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public void addDislike(@PathVariable int id, @PathVariable int userId) {
        log.info("Добавление дизлайка пользователем {} отзыву {}", userId, id);
        gradeReviewService.addDislike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Удаление лайка пользователем {} отзыву {}", userId, id);
        gradeReviewService.removeLike(id, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable int id, @PathVariable int userId) {
        log.info("Удаление дизлайка пользователем {} отзыву {}", userId, id);
        gradeReviewService.removeDislike(id, userId);
    }
}
