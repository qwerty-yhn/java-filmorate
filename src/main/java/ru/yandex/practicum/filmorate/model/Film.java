package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;

@Data
public class Film {

    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private int duration;

    private Set<Long> likes = new HashSet<>();

}
