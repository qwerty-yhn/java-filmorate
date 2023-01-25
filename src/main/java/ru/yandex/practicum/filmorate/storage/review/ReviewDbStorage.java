package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review addReview(Review review) {
        int id = (int) simpleSave(review);
        review.setReviewId(id);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        SqlRowSet rowsReview = jdbcTemplate.queryForRowSet("select * from REVIEWS where REVIEW_ID = ?", review.getReviewId());
        if (rowsReview.next()) {
            jdbcTemplate.update("update REVIEWS set CONTENT = ?, IS_POSITIVE = ? where REVIEW_ID = ?",
                    review.getContent(), review.getIsPositive(), review.getReviewId());
            String sql = "select * from reviews where review_id = ?";
            List<Review> reviews = jdbcTemplate.query(sql, (rs, rowNum) -> makeReviews(rs), review.getReviewId());
            return reviews.get(0);
        } else {
            throw new NotFoundException("Такого отзыва не существует!");
        }
    }

    @Override
    public void removeReview(int id) {
        SqlRowSet rowsReview = jdbcTemplate.queryForRowSet("select * from REVIEWS where REVIEW_ID = ?", id);
        if (rowsReview.next()) {
            jdbcTemplate.update("delete from USERS_LIKE_REVIEWS where REVIEW_ID = ?", id);
            jdbcTemplate.update("delete from REVIEWS where REVIEW_ID = ?", id);
        } else {
            throw new NotFoundException("Отзыв не найден!");
        }
    }

    @Override
    public Review getReviewById(int id) {
        String sql = "select * from reviews where review_id = ?";
        List<Review> reviews = jdbcTemplate.query(sql, (rs, rowNum) -> makeReviews(rs), id);
        if (reviews.isEmpty()) {
            throw new NotFoundException("Такого пользователя не существует!");
        } else {
            return reviews.get(0);
        }
    }

    @Override
    public LinkedHashSet<Review> getReviewsByCounts(int filmId, int count) {
        String sql = "select * from reviews where film_id = ? order by USEFUL DESC limit ?";
        return new LinkedHashSet<Review>(jdbcTemplate.query(sql, (rs, rowNum) -> makeReviews(rs), filmId, count));
    }

    @Override
    public LinkedHashSet<Review> getReviews() {
        String sql = "select * from reviews order by USEFUL DESC";
        return new LinkedHashSet<Review>(jdbcTemplate.query(sql, (rs, rowNum) -> makeReviews(rs)));
    }

    private Review makeReviews(ResultSet rs) throws SQLException {
        Review review = Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();

        return review;
    }

    private long simpleSave(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");
        return simpleJdbcInsert.executeAndReturnKey(review.toMap()).longValue();
    }
}
