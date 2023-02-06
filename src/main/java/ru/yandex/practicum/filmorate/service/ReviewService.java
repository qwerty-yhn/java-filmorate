package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final FeedService feedService;

    public Review addReview(Review review) {
        if (!review.getContent().isBlank()) {
            userService.getUserId(review.getUserId());
            filmService.getFilmId(review.getFilmId());

            Review review2 = reviewStorage.addReview(review);
            Event event = Event.builder()
                    .timestamp(System.currentTimeMillis())
                    .userId(review2.getUserId())
                    .eventType(EventTypes.REVIEW)
                    .operation(OperationTypes.ADD)
                    .entityId(review2.getReviewId())
                    .eventId(0)
                    .build();
            feedService.addEvent(event);

            return review2;
        } else {
            throw new VaidationExeption("Ошибка валидации");
        }
    }

    public Review updateReview(Review review) {

        Review review2 = reviewStorage.updateReview(review);
        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(review2.getUserId())
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.UPDATE)
                .entityId(review2.getReviewId())
                .eventId(0)
                .build();
        feedService.addEvent(event);

        return review2;
    }

    public void removeReview(int id) {

        Review review = reviewStorage.getReviewById(id);
        reviewStorage.removeReview(id);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(review.getUserId())
                .eventType(EventTypes.REVIEW)
                .operation(OperationTypes.REMOVE)
                .entityId(review.getReviewId())
                .eventId(0)
                .build();
        feedService.addEvent(event);
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
