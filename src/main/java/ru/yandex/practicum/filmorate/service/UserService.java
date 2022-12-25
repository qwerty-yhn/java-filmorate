package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final InMemoryUserStorage inMemoryUserStorage;

    public User createUser(User user){
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(User user){
        return inMemoryUserStorage.updateUser(user);
    }

    public List<User> getUsers(){
        return inMemoryUserStorage.getUsers();
    }

    public User getUsersById(Long id){
        return inMemoryUserStorage.getUserById(id);
    }

    public Set<Long> addFriend(Long id, Long friendId){
        Set<Long> s = inMemoryUserStorage.getUserById(id).getFriends();
        inMemoryUserStorage.getUserById(friendId);
        s.add(friendId);
        inMemoryUserStorage.getUserById(friendId).getFriends().add(id);
        return s;
    }

    public List<User> getFriend(Long id){
        Set<Long> idFriends = inMemoryUserStorage.getUserById(id).getFriends();
        List<User> userFriends = new ArrayList<>();

        for(Long inc : idFriends){
            userFriends.add(inMemoryUserStorage.getUserById(inc));
        }
        return userFriends;
    }

    public void deleteFriend(Long id, Long friendId){
        inMemoryUserStorage.getUserById(friendId);
        inMemoryUserStorage.getUserById(id).getFriends().remove(friendId);
    }

    public List<User> getCommonFriend(Long id, Long otherId){
        Set<Long> CurrentId = inMemoryUserStorage.getUserById(id).getFriends();
        Set<Long> OtherId  = inMemoryUserStorage.getUserById(otherId).getFriends();
        if(CurrentId == null || OtherId == null){
            return new ArrayList<>();
        }
        Set<Long> intersection = new HashSet<>(CurrentId);
        intersection.retainAll(OtherId);

        List<User> userFriends = new ArrayList<>();

        for(Long inc : intersection){
            userFriends.add(inMemoryUserStorage.getUserById(inc));
        }
        return userFriends;
    }
}
