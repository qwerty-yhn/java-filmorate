package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.LinkedHashSet;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("Идет процесс добавление нового отзыва: {}", review.getReviewId());
        return reviewService.addReview(review);
    }

    @PutMapping("/reviews")
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Идет процесс обновления отзыва: {}", review.getReviewId());
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/reviews/{id}")
    public void removeReview(@Positive @PathVariable int id) {
        log.info("Идет процесс удаления отзыва: {}", id);
        reviewService.removeReview(id);
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@Positive @PathVariable int id) {
        log.info("Идет процесс получения отзыва: {}", id);
        return reviewService.getReviewById(id);
    }

    @GetMapping("/reviews")
    public LinkedHashSet<Review> getReviews(@RequestParam(defaultValue = "0") Integer filmId, @RequestParam(defaultValue = "10") Integer count) {
        log.info("Идет процесс получения отзывов");
        return reviewService.getReviewsByCounts(filmId, count);
    }
}
