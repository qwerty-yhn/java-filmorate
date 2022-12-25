package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage{

    final private HashMap<Long, User> userHashMap = new HashMap<>();

    private Long generatorId = 0L;

    @Override
    public User createUser(User user) {
        assertUser(user);
        if (user.getName() == null || user.getName() == "") { user.setName( user.getLogin()); }
        user.setId(incIdUser());
        userHashMap.put(user.getId(), user);
        return userHashMap.get(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        if(userHashMap.containsKey(id)){
            userHashMap.remove(id);
        }
        else{
            throw new VaidationExeption("Нет такого user");
        }
    }

    @Override
    public User updateUser(User user) {
        assertUser(user);
        if (user.getName() == "") { user.setName( user.getLogin()); }
        if(userHashMap.containsKey(user.getId())){
            Long id = userHashMap.get(user.getId()).getId();
            user.setId(id);
            userHashMap.put(user.getId(), user);
            return userHashMap.get(user.getId());
        }
        else{
            throw new VaidationExeption("Нет такого user");
        }
    }

    public List<User> getUsers(){
        List<User> userList = new ArrayList<>();

        for(Long in: userHashMap.keySet()){
            userList.add(userHashMap.get(in));
        }
        return userList;
    }

    public User getUserById(Long id){
        if (userHashMap.containsKey(id)){
            return userHashMap.get(id);
        }else {
            throw new UserNotFoundException("Нет такого user");
        }
    }

    private Long incIdUser(){
        generatorId = generatorId + 1;
        return generatorId;
    }

    private void assertUser(User user){
        if(user.getLogin() == "" || user.getLogin().contains(" ")){
            throw new VaidationExeption("Логин не может быть пустым и содержать пробелы");
        }else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new VaidationExeption("Дата рождения не может быть в будущем");
        }
    }
}
