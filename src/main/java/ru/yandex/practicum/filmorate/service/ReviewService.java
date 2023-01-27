package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;

    public Review addReview(Review review) {
        if (!review.getContent().isBlank()) {
            userService.getUserId(review.getUserId());
            filmService.getFilmId(review.getFilmId());
            return reviewStorage.addReview(review);
        } else {
            throw new VaidationExeption("Ошибка валидации");
        }
    }

    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    public void removeReview(int id) {
        reviewStorage.removeReview(id);
    }

    public Review getReviewById(int id) {
        return reviewStorage.getReviewById(id);
    }

    public LinkedHashSet<Review> getReviewsByCounts(int filmId, int count) {
        if (filmId == 0) {
            return reviewStorage.getReviews();
        } else {
            return reviewStorage.getReviewsByCounts(filmId, count);
        }
    }
}
