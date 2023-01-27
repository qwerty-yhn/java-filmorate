package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.LinkedHashSet;

public interface ReviewStorage {
    Review addReview(Review review);


    Review updateReview(Review review);

    void removeReview(int id);

    Review getReviewById(int id);

    LinkedHashSet<Review> getReviewsByCounts(int filmId, int count);

    LinkedHashSet<Review> getReviews();
}
