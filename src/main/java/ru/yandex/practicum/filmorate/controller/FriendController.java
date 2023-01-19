package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class FriendController {
    @Autowired
    private FriendService friendService;
    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public List<Integer> addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("method = 'PUT' endpoint = '/users/{" + id + "}/friends/{" + friendId + "}'");
        return friendService.addFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriend(@PathVariable int id) {
        return friendService.getFriend(id);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        friendService.deleteFriend(id, friendId);
    }
}
