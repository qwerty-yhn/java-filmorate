package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class User {

    private int id;

    @Email
    private String email;

    private String login;

    private String name;

    private LocalDate birthday;
}
