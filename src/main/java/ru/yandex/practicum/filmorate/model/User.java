package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User {

    private Long id;

    @Email
    private String email;

    private String login;

    @NotNull
    private String name;

    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();
}
