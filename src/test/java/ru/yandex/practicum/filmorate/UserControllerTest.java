package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeption.VaidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    @Test
    public void testCreateUser(){
        UserController userController = new UserController();
        User user = new User();

        user.setId(1);
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(1990, 1,1));

        Assertions.assertEquals(user, userController.createUser(user));

        user.setId(1);
        user.setEmail("usermail.ru");
        user.setLogin("user");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(1990, 1,1));

        try {
            userController.createUser(user);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        user.setId(1);
        user.setEmail("");
        user.setLogin("user");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(1990, 1,1));

        try {
            userController.createUser(user);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        user.setId(1);
        user.setEmail("user@mail.ru");
        user.setLogin("");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(1990, 1,1));

        try {
            userController.createUser(user);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        user.setId(1);
        user.setEmail("user@mail.ru");
        user.setLogin("u s e r");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(1990, 1,1));

        try {
            userController.createUser(user);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }

        user.setId(1);
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 1,1));

        Assertions.assertEquals(user.getLogin(), userController.createUser(user).getName());

        user.setId(1);
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("Иван");
        user.setBirthday(LocalDate.of(2024, 1,1));

        try {
            userController.createUser(user);
            Assertions.assertTrue(false);
        } catch (VaidationExeption thrown) {
            Assertions.assertTrue(true);
        }
    }
}