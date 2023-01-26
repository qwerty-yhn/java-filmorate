package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController("FeedController")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;
    private final UserService userService;

    @GetMapping("/users/{id}/feed")
    public List<Event> findById(@PathVariable int id) {
        userService.getUserId(id);
        return feedService.getByUserId(id);
    }

}