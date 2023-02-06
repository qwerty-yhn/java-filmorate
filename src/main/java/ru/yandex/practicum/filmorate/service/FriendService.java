package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FriendService {
    @Autowired
    private final FriendDbStorage friendDbStorage;
    private final FeedService feedService;
    private final UserDbStorage userDbStorage;

    public List<Integer> addFriend(int id, int friendId){
        userDbStorage.getUserId(id);
        userDbStorage.getUserId(friendId);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(id)
                .eventType(EventTypes.FRIEND)
                .operation(OperationTypes.ADD)
                .entityId(friendId)
                .eventId(0)
                .build();
        feedService.addEvent(event);
        return friendDbStorage.addFriend(id, friendId);
    }

    public List<User> getFriend(int id){
        return friendDbStorage.getFriend(id);
    }

    public void deleteFriend(int id, int friendId){
        userDbStorage.getUserId(id);
        userDbStorage.getUserId(friendId);

        friendDbStorage.deleteFriend(id, friendId);

        Event event = Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(id)
                .eventType(EventTypes.FRIEND)
                .operation(OperationTypes.REMOVE)
                .entityId(friendId)
                .eventId(0)
                .build();
        feedService.addEvent(event);
    }
}
