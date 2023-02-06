package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.gradeReviews.GradeReviewsStorage;

@Service
@RequiredArgsConstructor
public class GradeReviewService {

    private final GradeReviewsStorage gradeReviewsStorage;
    private final ReviewService reviewService;
    private final UserService userService;

    public void addLike(int reviewId, int userId) {
        reviewService.getReviewById(reviewId);
        userService.getUserId(userId);
        gradeReviewsStorage.addLike(reviewId, userId);
    }

    public void addDislike(int reviewId, int userId) {
        reviewService.getReviewById(reviewId);
        userService.getUserId(userId);
        gradeReviewsStorage.addDislike(reviewId, userId);
    }

    public void removeLike(int reviewId, int userId) {
        reviewService.getReviewById(reviewId);
        userService.getUserId(userId);
        gradeReviewsStorage.removeLike(reviewId, userId);
    }

    public void removeDislike(int reviewId, int userId) {
        reviewService.getReviewById(reviewId);
        userService.getUserId(userId);
        gradeReviewsStorage.removeDislike(reviewId, userId);
    }
}
