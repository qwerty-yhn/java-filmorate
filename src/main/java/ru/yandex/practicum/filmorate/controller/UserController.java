package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> userHashMap = new HashMap<>();
    private int generatorId = 0;
    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user){
        log.info("Получен запрос GET/users");

        assertUser(user);

        if (user.getName() == null || user.getName() == "") { user.setName( user.getLogin()); }

        user.setId(incId());
        userHashMap.put(user.getId(), user);
        return userHashMap.get(user.getId());
    }
    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user){
        log.info("Получен запрос PUT/users");

        assertUser(user);

        if (user.getName() == "") { user.setName( user.getLogin()); }
        if(userHashMap.containsKey(user.getId())){
            int id = userHashMap.get(user.getId()).getId();
            user.setId(id);
            userHashMap.put(user.getId(), user);
            return userHashMap.get(user.getId());
        }
        else{
            log.error("Нет такого user");
            throw new VaidationExeption("Нет такого user");
        }
    }
    @GetMapping("/users")
    public List<User> getUsers(){
        List<User> userList = new ArrayList<>();
        log.info("Получен запрос /users");
        for(Integer in: userHashMap.keySet()){
            userList.add(userHashMap.get(in));
        }
        return userList;
    }
    private int incId(){
        generatorId = generatorId + 1;
        return generatorId;}
    private void assertUser(User user){
        if(user.getEmail() == "" || !(user.getEmail().contains("@"))){
            log.error("Электронная почта не может быть путой и должна содержать символ @");
            throw new VaidationExeption("Электронная почта не может быть путой и должна содержать символ @");
        }else if(user.getLogin() == "" || user.getLogin().contains(" ")){
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new VaidationExeption("Логин не может быть пустым и содержать пробелы");
        }else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new VaidationExeption("Дата рождения не может быть в будущем");
        }
    }
}
