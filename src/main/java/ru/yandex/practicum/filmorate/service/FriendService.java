package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FriendService {
    @Autowired
    private final FriendDbStorage friendDbStorage;
    public List<Integer> addFriend(int id, int friendId){
        return friendDbStorage.addFriend(id, friendId);
    }

    public List<User> getFriend(int id){
        return friendDbStorage.getFriend(id);
    }

    public void deleteFriend(int id, int friendId){
        friendDbStorage.deleteFriend(id, friendId);
    }
}
