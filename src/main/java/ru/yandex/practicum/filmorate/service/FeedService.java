package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;

    private final UserService userService;
	
    public List<Event> getByUserId(int userId) {
        userService.getUserId(userId);
        return feedStorage.getUserById(userId);
    }

    public Event addEvent(Event event) {
        return feedStorage.addEvent(event);
    }

}
