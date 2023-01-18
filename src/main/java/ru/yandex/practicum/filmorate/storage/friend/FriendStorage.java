package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    public List<Integer> addFriend(int id, int friendId);
    public List<User> getFriend(int id);
    public void deleteFriend(int id, int friendId);
}
