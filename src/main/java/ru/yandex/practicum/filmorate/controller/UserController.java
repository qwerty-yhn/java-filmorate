package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("method = 'POST' endpoint = '/users'");
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("method = 'PUT' endpoint = '/users'");
        return userService.updateUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUserId(@PathVariable int id) {
        return userService.getUserId(id);
    }

    @GetMapping("/users")
    public Collection<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriend(id, otherId);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("method = 'DELETE' endpoint = '/users/{userId}'");
        userService.deleteUser(userId);
    }
    @GetMapping("/users/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable int id) {

        return userService.getRecommendations(id);
    }
}
