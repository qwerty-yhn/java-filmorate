package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserDbStorage userDbStorage;

    public User createUser(User user){
        if (user.getName() == null || user.getName() == "") { user.setName( user.getLogin()); }
        return userDbStorage.createUser(user);
    }

    public User updateUser(User user){
        if (user.getName() == null || user.getName() == "") { user.setName( user.getLogin()); }
        return userDbStorage.updateUser(user);
    }

    public Collection<User> getUsers(){
        return userDbStorage.getUsers();
    }

    public User getUserId(int id){
        return userDbStorage.getUserId(id);
    }

    public List<Integer> addFriend(int id, int friendId){
        return userDbStorage.addFriend(id, friendId);
    }

    public List<User> getFriend(int id){
        return userDbStorage.getFriend(id);
    }

    public void deleteFriend(int id, int friendId){
        userDbStorage.deleteFriend(id, friendId);
    }
    public List<User> getCommonFriend(int id, int otherId){
        return userDbStorage.getCommonFriend(id, otherId);
    }
}
