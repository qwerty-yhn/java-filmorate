package ru.yandex.practicum.filmorate.storage.gradeReviews;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GradeReviewsDbStorage implements GradeReviewsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int reviewId, int userId) {
        jdbcTemplate.update("update REVIEWS set useful = useful + 1 where review_id = ?", reviewId);
        jdbcTemplate.update("insert into users_like_reviews (review_id, user_id, grade) values (?, ?, 'like')", reviewId, userId);
    }

    @Override
    public void addDislike(int reviewId, int userId) {
        jdbcTemplate.update("update reviews set useful = useful - 1 where review_id = ?", reviewId);
        jdbcTemplate.update("insert into users_like_reviews (review_id, user_id, grade) values (?, ?, 'dislike')", reviewId, userId);
    }

    @Override
    public void removeLike(int reviewId, int userId) {
        jdbcTemplate.update("delete from users_like_reviews where user_id = ? and review_id = ? and grade = 'like'", userId, reviewId);
        jdbcTemplate.update("update reviews set useful = useful - 1 where review_id = ?", reviewId);
    }

    @Override
    public void removeDislike(int reviewId, int userId) {
        jdbcTemplate.update("delete from users_like_reviews where user_id = ? and review_id = ? and grade = 'dislike'", userId, reviewId);
        jdbcTemplate.update("update reviews set useful = useful + 1 where review_id = ?", reviewId);
    }
}