package ru.yandex.practicum.filmorate.storage.gradeReviews;

public interface GradeReviewsStorage {

    void addLike(int review_id, int user_id);

    void addDislike(int review_id, int user_id);

    void removeLike(int review_id, int user_id);

    void removeDislike(int review_id, int user_id);
}
