package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

interface UserStorage {

    User createUser(User user);

    void deleteUser(Long id);

    User updateUser(User user);
}
