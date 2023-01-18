package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

interface UserStorage  {

    User createUser(User user);

    void deleteUser(int id);

    User updateUser(User user);
}
