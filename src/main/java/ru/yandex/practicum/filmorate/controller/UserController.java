package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user){
        log.info("method = 'POST' endpoint = '/users'");
        return userService.createUser(user);
    }
    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user){
        log.info("method = 'PUT' endpoint = '/users'");
        return userService.updateUser(user);
    }
    @GetMapping("/users/{id}")
    public User getUsersById(@PathVariable Long id){
        return userService.getUsersById(id);
    }
    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable Long id, @PathVariable Long otherId){
        return userService.getCommonFriend(id, otherId);
    }
    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public Set<Long> addFriend(@PathVariable Long id, @PathVariable Long friendId){
        log.info("method = 'PUT' endpoint = '/users/{" + id + "}/friends/{" + friendId + "}'");
        return userService.addFriend(id, friendId);
    }
    @GetMapping("/users/{id}/friends")
    public List<User> getFriend(@PathVariable Long id){
        return userService.getFriend(id);
    }
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId){
        userService.deleteFriend(id, friendId);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNegativeCount(final UserNotFoundException e) {
        return Map.of("error", "Нет такого user");
    }
}
