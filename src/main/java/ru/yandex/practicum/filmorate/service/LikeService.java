package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

@Service
@RequiredArgsConstructor
public class LikeService {
    @Autowired
    private final LikeDbStorage likeDbStorage;
    private final FeedService feedService;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    public Film addLikeToFilm(int id, int userId) {
        filmDbStorage.getFilmId(id);
        userDbStorage.getUserId(userId);

        Film film = likeDbStorage.addLikeToFilm(id, userId);

        Event event = Event.builder()
                            .timestamp(System.currentTimeMillis())
                            .userId(userId)
                            .eventType(EventTypes.LIKE)
                            .operation(OperationTypes.ADD)
                            .entityId(id)
                            .eventId(0)
                            .build();

        feedService.addEvent(event);
        return film;
    }

    public void deleteLikeToFilm(int id, int userId) {
        filmDbStorage.getFilmId(id);
        userDbStorage.getUserId(userId);

        likeDbStorage.deleteLikeToFilm(id,userId);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(EventTypes.LIKE)
                .operation(OperationTypes.REMOVE)
                .entityId(id)
                .eventId(0)
                .build();

        feedService.addEvent(event);
    }
}
